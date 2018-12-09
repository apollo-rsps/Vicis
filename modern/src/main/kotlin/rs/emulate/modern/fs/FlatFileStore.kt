package rs.emulate.modern.fs

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.modern.fs.FileStore.Companion.FILE_LEN
import java.io.FileNotFoundException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.Arrays
import java.util.regex.Matcher
import java.util.regex.Pattern

class FlatFileStore(
    private val root: Path,
    private val readWrite: Boolean
) : FileStore {

    private fun getIndexPath(index: Int): Path {
        require(index in 0 until FileStore.INDEX_LEN)
        return root.resolve(index.toString())
    }

    private fun getFilePath(index: Int, file: Int): Path {
        require(file in 0 until FileStore.FILE_LEN)
        return getIndexPath(index).resolve(file.toString() + DATA_FILE_SUFFIX)
    }

    override fun addIndex(index: Int) {
        val path = getIndexPath(index)
        if (!Files.isDirectory(path)) {
            Files.createDirectory(path)
        }
    }

    override fun containsIndex(index: Int): Boolean {
        val path = getIndexPath(index)
        return Files.isDirectory(path)
    }

    override fun removeIndex(index: Int) {
        val path = getIndexPath(index)

        if (Files.isDirectory(path)) {
            Files.walkFileTree(root, emptySet(), 1, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (file.endsWith(".dat")) {
                        Files.delete(file)
                    }

                    return FileVisitResult.CONTINUE
                }
            })

            Files.delete(path)
        }
    }

    override fun contains(index: Int, file: Int): Boolean {
        val path = getFilePath(index, file)
        return Files.isRegularFile(path)
    }

    override fun read(index: Int, file: Int): ByteBuf {
        val path = getFilePath(index, file)

        if (Files.isRegularFile(path)) {
            return Unpooled.wrappedBuffer(Files.readAllBytes(path)).asReadOnly()
        } else {
            throw FileNotFoundException()
        }
    }

    override fun write(index: Int, file: Int, buf: ByteBuf) {
        check(readWrite)

        Files.newOutputStream(getFilePath(index, file)).use { out ->
            val bytes: ByteArray
            val pos: Int
            val len = buf.readableBytes()

            if (buf.hasArray()) {
                bytes = buf.array()

                pos = buf.arrayOffset() + buf.readerIndex()
            } else {
                bytes = ByteArray(len)
                buf.getBytes(buf.readerIndex(), bytes)

                pos = 0
            }

            out.write(bytes, pos, len)
        }
    }

    override fun remove(index: Int, file: Int) {
        Files.deleteIfExists(getFilePath(index, file))
    }

    override fun listIndexes(): List<Int> {
        return Files.newDirectoryStream(root).use { stream ->
            stream.filter { Files.isDirectory(it) }
                .map { INDEX_DIR_PATTERN.matcher(it.fileName.toString()) }
                .filter(Matcher::matches)
                .map { it.group(1).toInt() }
                .filter { it in 0 until FILE_LEN }
        }
    }

    override fun listFiles(index: Int): List<Int> {
        val indexPath = getIndexPath(index)

        if (Files.isDirectory(indexPath)) {
            return Files.newDirectoryStream(indexPath).use { stream ->
                stream.filter { Files.isRegularFile(it) }
                    .map { DATA_FILE_PATTERN.matcher(it.fileName.toString()) }
                    .filter(Matcher::matches)
                    .map { it.group(1).toInt() }
                    .filter { it in 0 until FILE_LEN }
            }
        } else {
            throw FileNotFoundException()
        }
    }

    override fun close() {
        /* no-op */
    }

    companion object {
        private const val DATA_FILE_EXT = "dat"
        private const val DATA_FILE_SUFFIX = ".$DATA_FILE_EXT"
        private val INDEX_DIR_PATTERN = Pattern.compile("^([0-9]+)$")
        private val DATA_FILE_PATTERN = Pattern.compile("^([0-9]+)\\.$DATA_FILE_EXT$")

        fun open(root: Path, vararg options: FileStoreOption): FileStore {
            require(Files.isDirectory(root)) { "$root must be a directory" }
            return FlatFileStore(root, FileStoreOption.Write in Arrays.asList(*options))
        }

        fun create(root: Path, vararg options: FileStoreOption): FileStore {
            @Suppress("NAME_SHADOWING")
            var options = options

            if (!Files.exists(root)) {
                Files.createDirectory(root)
            }

            if (FileStoreOption.Write !in Arrays.asList(*options)) {
                options = Arrays.copyOf(options, options.size + 1)
                options[options.size - 1] = FileStoreOption.Write
            }

            return open(root, *options)
        }
    }
}
