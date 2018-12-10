package rs.emulate.legacy

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveCodec
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedShort
import rs.emulate.util.getUnsignedTriByte
import java.io.Closeable
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.util.ArrayList
import java.util.zip.CRC32

/**
 * A file system based on top of the operating system's file system. It consists of a data file and multiple index
 * files. Index files point to blocks in the data file, which contain the actual data.
 *
 * @param base The base directory.
 * @param mode The [AccessMode].
 * @throws FileNotFoundException If the data files could not be found.
 */
class IndexedFileSystem(base: Path, private val mode: AccessMode) : Closeable {

    private val data: RandomAccessFile?

    private val indices: List<RandomAccessFile>

    /**
     * The cached CRC table.
     */
    private var crcs: ByteBuf? = null

    val crcTable: ByteBuf
        get() {
            if (readOnly) {
                synchronized(this) {
                    crcs?.let { return it.duplicate() }
                }

                val archives = getFileCount(0)
                var hash = 1234

                val buffer = Unpooled.buffer((archives + 1) * Integer.BYTES)
                val crc = CRC32()

                for (file in 1 until archives) {
                    val bytes = get(0, file).readableBytes()
                    crc.update(bytes)

                    val value = crc.value.toInt()
                    buffer.writeInt(value)
                    hash = (hash shl 1) + value
                }

                buffer.writeInt(hash)

                synchronized(this) {
                    crcs = buffer
                }

                return buffer.asReadOnly()
            }

            throw IOException("Cannot get CRC table from a writable file system.")
        }

    val indexCount: Int
        get() = indices.size

    val readOnly: Boolean
        get() = mode === AccessMode.READ

    init {
        indices = getIndexFiles(base)
        data = getDataFile(base)
    }

    fun getArchive(type: Int, file: Int): Archive {
        return ArchiveCodec.decode(get(type, file))
    }

    operator fun get(type: Int, file: Int): ByteBuf {
        val (size, block) = getIndex(type, file)
        val buffer = ByteBuffer.allocate(size)

        var position = (block * FileSystemConstants.BLOCK_SIZE).toLong()
        var read = 0
        var blocks = size / FileSystemConstants.CHUNK_SIZE

        if (size % FileSystemConstants.CHUNK_SIZE != 0) {
            blocks++
        }

        for (id in 0 until blocks) {
            val header = ByteBuffer.allocate(FileSystemConstants.HEADER_SIZE)
            synchronized(data!!) {
                data.seek(position)
                data.readFully(header.array())
            }

            position += FileSystemConstants.HEADER_SIZE.toLong()

            val nextFile = header.getUnsignedShort()
            val currentChunk = header.getUnsignedShort()
            val nextBlock = header.getUnsignedTriByte()
            val nextType = header.getUnsignedByte()

            check(id == currentChunk) { "Chunk id mismatch: id=$id, chunk=$currentChunk, type=$type, file=$file." }
            val chunkSize = Math.min(FileSystemConstants.CHUNK_SIZE, size - read)

            val chunk = ByteBuffer.allocate(chunkSize)
            synchronized(data) {
                data.seek(position)
                data.readFully(chunk.array())
            }

            buffer.put(chunk)

            read += chunkSize
            position = (nextBlock * FileSystemConstants.BLOCK_SIZE).toLong()

            if (size > read) {
                check(nextType == type + 1) { "File type mismatch." }
                check(nextFile == file) { "File id mismatch." }
            }
        }

        buffer.flip()
        return Unpooled.wrappedBuffer(buffer) // TODO do this properly
    }

    fun getIndex(type: Int, file: Int): Index {
        Preconditions.checkElementIndex(type, indices.size, "File descriptor type out of bounds.")

        val index = indices[type]
        val position = (file * Index.BYTES).toLong()
        require(position >= 0 && index.length() >= position + Index.BYTES) { "Could not find find index." }

        val bytes = ByteArray(Index.BYTES)
        synchronized(index) {
            index.seek(position)
            index.readFully(bytes)
        }

        return IndexCodec.decode(Unpooled.wrappedBuffer(bytes))
    }

    fun getFileCount(type: Int): Int {
        require(type in indices.indices) { "File type out of bounds." }

        val index = indices[type]
        synchronized(index) {
            return (index.length() / Index.BYTES).toInt()
        }
    }

    override fun close() {
        if (data != null) {
            synchronized(data) {
                data.close()
            }
        }

        for (index in indices) {
            synchronized(index) {
                index.close()
            }
        }
    }

    private fun getDataFile(base: Path): RandomAccessFile {
        val resources = base.resolve("main_file_cache.dat")

        if (!Files.exists(resources) || Files.isDirectory(resources)) {
            throw FileNotFoundException("No data file present.")
        }

        return RandomAccessFile(resources.toFile(), mode.asUnix())
    }

    private fun getIndexFiles(base: Path): List<RandomAccessFile> {
        val indices = ArrayList<RandomAccessFile>()

        for (id in 0 until INDEX_COUNT) {
            val index = base.resolve("main_file_cache.idx$id")

            if (Files.exists(index) && !Files.isDirectory(index)) {
                indices.add(RandomAccessFile(index.toFile(), mode.asUnix()))
            }
        }

        if (indices.isEmpty()) {
            throw FileNotFoundException("No index files present.")
        }

        return ImmutableList.copyOf(indices)
    }

    companion object {

        /**
         * The maximum amount of indices.
         */
        private const val INDEX_COUNT = 256
    }

}
