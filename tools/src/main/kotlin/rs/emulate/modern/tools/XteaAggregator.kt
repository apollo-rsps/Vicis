package rs.emulate.modern.tools

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import rs.emulate.modern.Container
import rs.emulate.modern.FileStore
import rs.emulate.modern.ReferenceTable
import rs.emulate.shared.util.CacheStringUtils
import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.crypto.Xtea
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.FileWriter
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.Arrays
import java.util.regex.Pattern
import java.util.zip.CRC32
import javax.imageio.ImageIO
import kotlin.experimental.and

object XteaAggregator {

    private const val MAP_INDEX = 5

    private const val MAP_SIZE = 256

    private const val RT_INDEX = 255

    private enum class ProcessResult(color: Color) {
        HAVE_KEY(Color.GREEN),
        PLAINTEXT_EMPTY(Color.BLUE),
        HAVE_KEY_FROM_OTHER_CACHE(Color.YELLOW),
        FAILED(Color.RED);

        val rgb: Int = color.rgb

    }

    private class CacheRef(val store: FileStore, val table: ReferenceTable, val rs3: Boolean) {

        fun findLandscapeFile(x: Int, y: Int): Int {
            if (rs3) {
                return y shl 7 or x
            }

            val landscapeFile = "l" + x + "_" + y
            val landscapeIdentifier = CacheStringUtils.hash(landscapeFile)

            for (id in 0 until table.capacity()) {
                val entry = table.getEntry(id) ?: continue

                if (entry.identifier == landscapeIdentifier)
                    return id
            }

            return -1 /* doesn't exist */
        }
    }

    private class Key(val k: IntArray) {

        val isZero: Boolean
            get() = k[0] == 0 && k[1] == 0 && k[2] == 0 && k[3] == 0

        init {
            if (k.size != 4)
                throw IllegalArgumentException()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (other == null || javaClass != other.javaClass)
                return false

            val key = other as Key?

            return Arrays.equals(k, key!!.k)
        }

        override fun hashCode(): Int {
            return Arrays.hashCode(k)
        }

        companion object {
            val ZERO = Key(IntArray(4))
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("Type\tFile\tVer\tVe'\tCRC\t\t\tCRC'")

        val store = FileStore.open("./game/data/cache")
        var rtContainer = Container.decode(store.read(RT_INDEX, MAP_INDEX))
        val rt = ReferenceTable.decode(rtContainer.data)

        val possibleKeys = readPossibleKeys()
        val otherCaches = findOtherCaches()
        val map = BufferedImage(MAP_SIZE, MAP_SIZE, BufferedImage.TYPE_INT_ARGB)

        var total = 0
        var haveKey = 0
        var plaintextEmpty = 0
        var haveKeyFromOtherCache = 0

        var rewriteReferenceTable = false

        for (x in 0 until MAP_SIZE) {
            for (y in 0 until MAP_SIZE) {
                val cache = CacheRef(store, rt, false)
                val id = cache.findLandscapeFile(x, y)

                if (id != -1) {
                    val buf = store.read(MAP_INDEX, id).byteBuffer
                    val result = processLandscapeFile(store, rt, id, x, y, buf, possibleKeys, otherCaches)

                    if (result == ProcessResult.PLAINTEXT_EMPTY || result == ProcessResult.HAVE_KEY_FROM_OTHER_CACHE)
                        rewriteReferenceTable = true

                    map.setRGB(x, MAP_SIZE - y, result.rgb)

                    total++

                    if (result == ProcessResult.HAVE_KEY)
                        haveKey++

                    if (result == ProcessResult.PLAINTEXT_EMPTY)
                        plaintextEmpty++

                    if (result == ProcessResult.HAVE_KEY_FROM_OTHER_CACHE)
                        haveKeyFromOtherCache++
                }
            }
        }

        if (rewriteReferenceTable) {
            val version = rt.version
            rt.version = version + 1
            println("(Reference table version: " + version + " -> " + rt.version + ")")

            rtContainer = Container(rtContainer.type, rt.encode())

            store.write(RT_INDEX, MAP_INDEX, rtContainer.encode())
        }

        ImageIO.write(map, "png", File("cache/data/map.png"))

        val successful = haveKey + plaintextEmpty + haveKeyFromOtherCache
        val failed = total - successful

        println(successful.toString() + " / " + total + " files successful (" + failed + " failed)")
        println("    Have key for:                  $haveKey files")
        println("    Plaintext looks empty for:     $plaintextEmpty files")
        println("    Have key from other cache for: $haveKeyFromOtherCache files")
    }

    private fun findOtherCaches(): ImmutableList<CacheRef> {
        val caches = ImmutableList.builder<CacheRef>()

        for (f in File("cache/data/other-caches").listFiles()!!) {
            if (!f.isDirectory)
                continue

            val store = FileStore.open(f.toPath()) // TODO
            val table = ReferenceTable.decode(Container.decode(store.read(RT_INDEX, MAP_INDEX)).data)
            caches.add(CacheRef(store, table, f.name == "rs3"))
        }

        return caches.build()
    }

    private fun isEmpty(buf: ByteBuffer): Boolean {
        /*
         * Lots of the files we don't have keys for are sea, which contains no objects. Looking at the encrypted and
         * compressed length we can guess if the file contains no objects.
         */
        val type = (buf.get(buf.position()) and 0xFF.toByte()).toInt()
        return if (type == Container.COMPRESSION_NONE) {
            /*
             * A landscape file with no objects has a single zero-valued smart, which occupies 1 byte. We add on the 4
             * bytes uncompressed length, the 1 byte type and the 2 byte version.
             */
            buf.remaining() == 8
        } else if (type == Container.COMPRESSION_GZIP) {
            /*
             * gzip-compressed in Java, a single byte occupies 21 bytes. We add the 4 byte uncompressed length, the 4
             * byte compressed length, the 1 byte type and the 2 byte version.
             */
            buf.remaining() == 32
        } else if (type == Container.COMPRESSION_BZIP2) {
            /*
             * bzip2-compressed in Java excluding the header, a single byte occupies 33 bytes. We add the 4 byte
             * uncompressed length, the 4 byte compressed length, the 1 byte type and the 2 byte version.
             */
            buf.remaining() == 44
        } else {
            throw IllegalArgumentException("Invalid container type.")
        }
    }

    private fun isKeyValid(buf: ByteBuffer, key: Key, rs3: Boolean): Boolean {
        /*
         * Files in RS3 are not encrypted. Return true if it looks non-empty (i.e. has more than the container header
         * and trailer) and if the current key we're using is the ZERO key indicating that decryption is not to be
         * performed.
         */
        if (rs3) {
            return key == Key.ZERO && buf.remaining() > 7
        }

        /*
         * This functions attempts to check if a key is probably valid whilst doing the absolute minimum amount of work
         * by only examining the gzip/bzip2 headers, without actually decompressing anything.
         *
         * In both cases we want to decrypt two blocks of ciphertext.
         *
         * gzip: 4 byte client uncompressed len + 10 byte GZIP header bzip2: 4 byte client uncompressed len + 6 byte
         * block magic
         *
         * Both cases round up to 16 bytes (two XTEA blocks).
         */
        val clone = ByteBuffer.allocate(16)

        val type = (buf.get(buf.position()) and 0xFF.toByte()).toInt()
        if (type == Container.COMPRESSION_NONE) {
            throw UnsupportedOperationException("Can't test uncompressed containers for key validity.")
        }

        val oldPosition = buf.position()
        val oldLimit = buf.limit()

        buf.position(buf.position() + 5) /* skip type and compressed length */
        buf.limit(buf.position() + clone.limit())

        clone.put(buf.slice())
        clone.rewind()

        buf.position(oldPosition)
        buf.limit(oldLimit)

        if (!key.isZero) {
            Xtea.decipher(DataBuffer.wrap(clone), clone.position(), clone.limit(), key.k)
        }

        val uncompressedLength = clone.int
        if (uncompressedLength < 0)
            return false

        if (type == Container.COMPRESSION_GZIP) {
            val magic = (clone.short and 0xFFFF.toShort()).toInt()
            if (magic != 0x1F8B)
                return false

            val compressionMethod = (clone.get() and 0xFF.toByte()).toInt()
            if (compressionMethod != 0x08)
                return false

            val flags = (clone.get() and 0xFF.toByte()).toInt()
            if (flags != 0)
                return false

            val time = clone.int
            if (time != 0)
                return false

            val extraFlags = (clone.get() and 0xFF.toByte()).toInt()
            if (extraFlags != 0)
                return false

            val os = (clone.get() and 0xFF.toByte()).toInt()

            return os == 0
        } else if (type == Container.COMPRESSION_BZIP2) {
            val magic = ByteArray(6)
            clone.get(magic)

            val expectedMagic = byteArrayOf(0x31, 0x41, 0x59, 0x26, 0x53, 0x59)
            return Arrays.equals(magic, expectedMagic)
        } else {
            throw IllegalArgumentException("Invalid container type.")
        }
    }

    private fun processLandscapeFile(
        store: FileStore, rt: ReferenceTable, id: Int, x: Int, y: Int, buf: ByteBuffer,
        possibleKeys: Set<Key>, otherCaches: List<CacheRef>
    ): ProcessResult {
        var buf = buf
        val region = x shl 8 or y
        val absX = x * 64 + 32
        val absY = y * 64 + 32

        for (key in possibleKeys) {
            if (isKeyValid(buf, key, false)) {
                writeKey(region, key)
                return ProcessResult.HAVE_KEY
            }
        }

        if (isEmpty(buf)) {
            val entry = rt.getEntry(id)!!

            val previousVersion = entry.version
            val version = previousVersion + 1
            entry.version = version

            buf = ByteBuffer.allocate(1)
            buf.put(0.toByte())
            buf.flip()

            val container = Container(Container.COMPRESSION_GZIP, DataBuffer.wrap(buf), version)
            buf = container.encode().byteBuffer

            val previousCrc = entry.crc

            val bytes = ByteArray(buf.limit() - 2) /* exclude version from CRC */
            buf.slice().get(bytes)

            val crc = CRC32()
            crc.update(bytes)
            entry.crc = crc.value.toInt()

            store.write(MAP_INDEX, id, buf)

            println("$MAP_INDEX\t$id\t$previousVersion\t$version\t$previousCrc\t${entry.crc}\t($absX, $absY)")

            writeKey(region, Key.ZERO)
            return ProcessResult.PLAINTEXT_EMPTY
        }

        /* check if we have a valid key for a version of the file from a different cache */
        for (otherCache in otherCaches) {
            val otherId = otherCache.findLandscapeFile(x, y)
            if (otherId != -1) {
                buf = otherCache.store.read(MAP_INDEX, otherId).byteBuffer

                for (key in possibleKeys) {
                    if (isKeyValid(buf, key, otherCache.rs3)) {
                        val entry = rt.getEntry(id)!!

                        val previousVersion = entry.version
                        val version = previousVersion + 1
                        entry.version = version

                        buf.putShort(buf.limit() - 2, version.toShort())

                        val previousCrc = entry.crc

                        val bytes = ByteArray(buf.limit() - 2)
                        buf.slice().get(bytes)

                        val crc = CRC32()
                        crc.update(bytes)
                        entry.crc = crc.value.toInt()

                        store.write(MAP_INDEX, id, buf)

                        println(
                            "$MAP_INDEX\t$id\t$previousVersion\t$version\t$previousCrc\t${entry.crc}\t($absX, $absY)")

                        writeKey(region, key)

                        // TODO copy the mX_Y file too?
                        // TODO how to select the best file? in some cases we have a choice from multiple versions
                        return ProcessResult.HAVE_KEY_FROM_OTHER_CACHE
                    }
                }
            }
        }

        println("Missing: $absX, $absY ($region.txt)")
        return ProcessResult.FAILED
    }

    private fun readPossibleKeys(): ImmutableSet<Key> {
        val builder = ImmutableSet.builder<Key>()
        builder.add(Key.ZERO) // zero key used to disable encryption

        Files.walkFileTree(Paths.get("cache/data/landscape-keys"), object : SimpleFileVisitor<Path>() {

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (file.fileName.toString().endsWith(".jcm")) {
                    Files.newBufferedReader(file, StandardCharsets.UTF_8).use { reader ->
                        var line = reader.readLine()

                        while (line != null) {
                            if (!line.startsWith("--")) {
                                val parts = line.split(" ".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                                if (parts.size >= 4) {
                                    val k = parts[3].split(
                                        "\\.".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                                    val ki = IntArray(4)
                                    for (i in ki.indices) {
                                        ki[i] = Integer.parseInt(k[i])
                                    }
                                    builder.add(Key(ki))
                                }
                            }

                            line = reader.readLine()
                        }
                    }
                } else if (file.fileName.toString().endsWith(".txt")) {
                    Files.newBufferedReader(file, StandardCharsets.UTF_8).use { reader ->
                        outer@ while (true) {
                            val k = IntArray(4)
                            for (i in k.indices) {
                                val line = reader.readLine()
                                if (line == null || line.isEmpty())
                                    break@outer
                                k[i] = Integer.parseInt(line)
                            }
                            builder.add(Key(k))
                        }
                    }
                } else if (file.fileName.toString().endsWith(".dat") || file.fileName.toString().endsWith(".bin")) {
                    DataInputStream(BufferedInputStream(Files.newInputStream(file))).use { `is` ->
                        while (true) {
                            try {
                                `is`.readShort() // region id
                                val k = IntArray(4)
                                for (i in k.indices) {
                                    k[i] = `is`.readInt()
                                }
                                builder.add(Key(k))
                            } catch (ex: EOFException) {
                                break
                            }

                        }
                    }
                } else if (file.fileName.toString().endsWith(".sql")) {
                    val f = String(Files.readAllBytes(file), StandardCharsets.UTF_8)
                    val matcher = Pattern.compile("\\([0-9]+,([0-9-]+),([0-9-]+),([0-9-]+),([0-9-]+)\\)").matcher(f)
                    while (matcher.find()) {
                        val k = intArrayOf(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
                        builder.add(Key(k))
                    }
                }
                return FileVisitResult.CONTINUE
            }
        })

        return builder.build()
    }

    private fun writeKey(region: Int, key: Key) {
        BufferedWriter(FileWriter("game/data/landscape-keys/$region.txt")).use { writer ->
            key.k.indices.forEach { i -> writer.write(key.k[i].toString() + "\n") }
        }
    }

}
