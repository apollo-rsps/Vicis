package rs.emulate.modern.codec.store

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.modern.codec.store.FileStore.Companion.FILE_LEN
import rs.emulate.modern.codec.store.FileStore.Companion.INDEX_LEN
import rs.emulate.util.getUnsignedMedium
import rs.emulate.util.putMedium
import rs.emulate.util.putTriByte
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.nio.file.StandardOpenOption.READ
import java.nio.file.StandardOpenOption.WRITE
import java.util.*
import java.util.regex.Pattern

class JagexFileStore(
    private val root: Path,
    private val options: List<OpenOption>,
    private val dataChannel: PagedFile,
    private val indexChannels: Array<PagedFile?>,
    private val strict: Boolean = false
) : FileStore {

    private val indexBuffer = ByteBuffer.allocate(INDEX_BLOCK_SIZE)
    private val dataBuffer = ByteBuffer.allocate(DATA_BLOCK_SIZE)

    init {
        require(indexChannels.size == INDEX_LEN)
    }

    override fun addIndex(index: Int) {
        if (WRITE !in options) {
            throw IOException("Cannot create an index of the cache is read-only")
        }

        require(index in 0 until indexChannels.size)

        if (indexChannels[index] == null) {
            val file = root.resolve(INDEX_FILE_PREFIX + index)
            val channel = FileChannel.open(file, StandardOpenOption.CREATE, READ, WRITE)
            indexChannels[index] = PagedFile(channel, INDEX_BLOCK_SIZE, 1, true)
        }
    }

    override fun containsIndex(index: Int): Boolean {
        return indexChannels[index] != null
    }

    override fun removeIndex(index: Int) {
        if (indexChannels[index] != null) {
            Files.delete(root.resolve(INDEX_FILE_PREFIX + index))
            indexChannels[index] = null
        }
    }

    override fun contains(index: Int, file: Int): Boolean {
        require(index in 0 until indexChannels.size)
        require(file in 0 until FILE_LEN)

        val indexChannel = indexChannels[index] ?: return false

        val pos = file.toLong()
        if (pos >= indexChannel.size()) {
            return false
        }

        indexBuffer.clear()
        indexChannel.read(indexBuffer, pos)
        indexBuffer.flip()

        /* skip size */
        indexBuffer.position(indexBuffer.position() + 3)

        val sector = indexBuffer.getUnsignedMedium()
        return sector != 0
    }

    override fun read(index: Int, file: Int): ByteBuf {
        require(index in 0 until indexChannels.size)
        require(file in 0 until FILE_LEN)

        val indexChannel = indexChannels[index] ?: throw FileNotFoundException()

        var pos = file.toLong()
        if (pos >= indexChannel.size()) {
            throw FileNotFoundException()
        }

        indexBuffer.clear()
        indexChannel.read(indexBuffer, pos)
        indexBuffer.flip()

        val size = indexBuffer.getUnsignedMedium()
        if (size < 0) {
            throw IOException("File size is negative")
        }

        val buf = ByteBuffer.allocate(size)

        var sector = indexBuffer.getUnsignedMedium()
        if (sector == 0) {
            throw FileNotFoundException()
        }

        var counter = 0

        do {
            if (sector <= 0) {
                throw IOException("Sector is not positive")
            }

            pos = sector.toLong()
            if (pos >= dataChannel.size()) {
                throw IOException("Sector is outside data file")
            }

            dataBuffer.clear()
            dataChannel.read(dataBuffer, pos)
            dataBuffer.flip()

            val file0 = dataBuffer.short.toInt() and 0xFFFF
            val counter0 = dataBuffer.short.toInt() and 0xFFFF
            val sector0 = dataBuffer.getUnsignedMedium()
            val index0 = dataBuffer.get().toInt() and 0xFF

            if (file0 != file) {
                throw IOException("File mismatch")
            }

            if (index0 != index) {
                throw IOException("Index mismatch")
            }

            if (counter0 != counter) {
                throw IOException("Counter mismatch")
            }

            val writableBytes = buf.remaining()
            if (writableBytes < DATA_SIZE) {
                dataBuffer.limit(HEADER_SIZE + writableBytes)
            }

            buf.put(dataBuffer)

            sector = sector0
            counter++
        } while (buf.hasRemaining())

        if (strict && sector != 0) {
            throw IOException("Trailing sector must be zero")
        }

        buf.flip()
        return Unpooled.wrappedBuffer(buf).asReadOnly()
    }

    override fun write(index: Int, file: Int, buf: ByteBuf) {
        require(buf.readableBytes() <= MAX_FILE_SIZE)
        require(index in 0 until indexChannels.size)
        require(file in 0 until FILE_LEN)

        val indexChannel = indexChannels[index] ?: throw FileNotFoundException()

        var pos = file.toLong()

        val data = buf.nioBuffer()

        val size = data.remaining()
        var existingSize = 0
        var sector = 0

        /* get existing file sector/size, if there is one */
        if (pos < indexChannel.size()) {
            indexBuffer.clear()
            indexChannel.read(indexBuffer, pos)
            indexBuffer.flip()

            existingSize = indexBuffer.getUnsignedMedium()
            if (existingSize < 0) {
                throw IOException("Existing file size is negative")
            }

            sector = indexBuffer.getUnsignedMedium()
        }

        var overwriting = sector != 0

        /* if we're not overwriting, allocate first sector at the end of the data file */
        if (!overwriting) {
            sector = dataChannel.size().toInt() // TODO check if sector is greater than max

            /* zero is reserved */
            if (sector == 0) {
                sector = 1
            }
        }

        /* write index entry */
        indexBuffer.clear()

        indexBuffer.putMedium(size)
        indexBuffer.putMedium(sector)

        indexBuffer.flip()

        indexChannel.write(indexBuffer, pos)

        var counter = 0

        do {
            if (sector <= 0) {
                throw IOException("Sector is not positive")
            }

            pos = sector.toLong()

            if (overwriting) {
                /* read existing block header */
                if (pos >= dataChannel.size()) {
                    throw IOException("Sector is outside data file")
                }

                dataBuffer.clear()
                dataChannel.read(dataBuffer, pos)
                dataBuffer.flip()

                val file0 = dataBuffer.short.toInt() and 0xFFFF
                val counter0 = dataBuffer.short.toInt() and 0xFFFF
                val sector0 = dataBuffer.getUnsignedMedium()
                val index0 = dataBuffer.get().toInt() and 0xFF

                if (file0 != file) {
                    throw IOException("File mismatch")
                }

                if (index0 != index) {
                    throw IOException("Index mismatch")
                }

                if (counter0 != counter) {
                    throw IOException("Counter mismatch")
                }

                sector = sector0
                existingSize -= DATA_SIZE
            } else {
                sector++
            }

            if (overwriting && sector == 0) {
                overwriting = false

                /* check if we were supposed to reach the EOF */
                if (existingSize > 0) {
                    throw IOException("Trailing sector of existing file is non-zero")
                }

                sector = dataChannel.size().toInt() // TODO check if sector is greater than max
            }

            /* set the next sector to zero if this is the last sector */
            if (data.remaining() <= DATA_SIZE) {
                sector = 0
            }

            dataBuffer.clear()

            dataBuffer.putShort(file.toShort())
            dataBuffer.putShort(counter.toShort())
            dataBuffer.putMedium(sector)
            dataBuffer.put(index.toByte())

            var readableBytes = data.remaining()
            if (readableBytes > DATA_SIZE) {
                readableBytes = DATA_SIZE
            }

            val temp = ByteArray(DATA_SIZE)
            data.get(temp, 0, readableBytes)

            dataBuffer.put(temp).flip()
            dataChannel.write(dataBuffer, pos)

            counter++
        } while (data.hasRemaining())
    }

    override fun remove(index: Int, file: Int) {
        require(index in 0 until indexChannels.size)
        require(file in 0 until FILE_LEN)

        val indexChannel = indexChannels[index] ?: throw FileNotFoundException()
        val pos = file.toLong()

        if (pos < indexChannel.size()) {
            indexBuffer.clear()
            indexBuffer.putTriByte(0).putTriByte(0) // 6 bytes
            indexBuffer.flip()

            indexChannel.write(indexBuffer, pos)
        }
    }

    override fun listIndexes(): List<Int> {
        return (0 until INDEX_LEN).filter { indexChannels[it] != null }
    }

    override fun listFiles(index: Int): List<Int> {
        val indexChannel = indexChannels[index] ?: throw FileNotFoundException()
        val size = indexChannel.size()

        return (0 until size).filter { file ->
            indexBuffer.clear()
            indexChannel.read(indexBuffer, file)
            indexBuffer.flip()

            /* skip size */
            indexBuffer.position(indexBuffer.position() + 3)

            val sector = indexBuffer.getUnsignedMedium()
            sector != 0
        }.map(Long::toInt)
    }

    override fun close() {
        dataChannel.close()

        indexChannels.asSequence()
            .filterNotNull()
            .forEach(PagedFile::close)
    }

    companion object {
        private const val PREFIX = "main_file_cache"

        const val DATA_FILE = "$PREFIX.dat2"
        private const val INDEX_FILE_PREFIX = "$PREFIX.idx"

        private val INDEX_FILE_PATTERN = Pattern.compile("^$PREFIX\\.idx([0-9]+)$")

        private val READ_WRITE_OPTIONS = arrayOf<OpenOption>(READ, WRITE)
        private val READ_ONLY_OPTIONS = arrayOf<OpenOption>(READ)

        private const val INDEX_BLOCK_SIZE = 6
        private const val HEADER_SIZE = 8
        private const val DATA_SIZE = 512
        private const val DATA_BLOCK_SIZE = HEADER_SIZE + DATA_SIZE

        private const val MAX_FILE_SIZE = 65536 * DATA_SIZE

        private const val INDEX_PAGES = 1
        private const val DATA_PAGES = 16

        fun open(root: Path, vararg options: FileStoreOption): FileStore {
            require(Files.isDirectory(root))

            val dataFile = root.resolve(DATA_FILE)
            require(Files.exists(dataFile))

            val indexFiles = arrayOfNulls<Path>(INDEX_LEN)

            Files.newDirectoryStream(root).use { stream ->
                for (file in stream) {
                    val matcher = INDEX_FILE_PATTERN.matcher(file.fileName.toString())

                    if (matcher.matches()) {
                        val index = matcher.group(1).toInt()
                        if (index >= 0 && index < indexFiles.size) {
                            indexFiles[index] = file
                        }
                    }
                }
            }

            val optionList = Arrays.asList(*options)
            val nioOptions = if (FileStoreOption.Write in optionList) READ_WRITE_OPTIONS else READ_ONLY_OPTIONS
            val strict = FileStoreOption.Lenient !in optionList

            val dataChannel = PagedFile(FileChannel.open(dataFile, *nioOptions), DATA_BLOCK_SIZE, DATA_PAGES, strict)

            val indexChannels = arrayOfNulls<PagedFile>(indexFiles.size)
            for (i in indexFiles.indices) {
                val file = indexFiles[i]
                if (file != null) {
                    val channel = FileChannel.open(file, *nioOptions)
                    indexChannels[i] = PagedFile(channel, INDEX_BLOCK_SIZE, INDEX_PAGES, strict)
                }
            }

            return JagexFileStore(root, nioOptions.toList(), dataChannel, indexChannels, strict)
        }

        fun create(root: Path, vararg options: FileStoreOption): FileStore {
            @Suppress("NAME_SHADOWING")
            var options = options

            if (!Files.exists(root)) {
                Files.createDirectory(root)
            }

            val dataFile = root.resolve(DATA_FILE)
            if (!Files.exists(dataFile)) {
                Files.createFile(dataFile)
            }

            if (!listOf(*options).contains(FileStoreOption.Write)) {
                options = Arrays.copyOf(options, options.size + 1)
                options[options.size - 1] = FileStoreOption.Write
            }

            return open(root, *options)
        }
    }
}
