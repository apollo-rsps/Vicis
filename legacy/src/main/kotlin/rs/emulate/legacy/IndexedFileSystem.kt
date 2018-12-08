package rs.emulate.legacy

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveCodec
import rs.emulate.shared.util.getRemainingBytes
import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedShort
import rs.emulate.shared.util.getUnsignedTriByte
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

    /**
     * The data file.
     */
    private val data: RandomAccessFile?

    /**
     * The index files.
     */
    private val indices: List<RandomAccessFile>

    /**
     * The cached CRC table.
     */
    private var crcs: ByteBuffer? = null

    /**
     * The CRC table.
     */
    val crcTable: ByteBuffer
        get() {
            if (readOnly) {
                synchronized(this) {
                    crcs?.let { return it.duplicate() }
                }

                val archives = getFileCount(0)
                var hash = 1234

                val buffer = ByteBuffer.allocate((archives + 1) * Integer.BYTES)
                val crc = CRC32()

                for (file in 1 until archives) {
                    val bytes = getFile(0, file).getRemainingBytes()
                    crc.update(bytes)

                    val value = crc.value.toInt()
                    buffer.putInt(value)
                    hash = (hash shl 1) + value
                }

                buffer.putInt(hash)
                buffer.flip()

                synchronized(this) {
                    val duplicate = buffer.asReadOnlyBuffer()
                    crcs = duplicate
                    return duplicate
                }
            }

            throw IOException("Cannot get CRC table from a writable file system.")
        }

    /**
     * Gets the amount of indices in this IndexedFileSystem.
     */
    val indexCount: Int
        get() = indices.size

    /**
     * Checks if this [IndexedFileSystem] is read only.
     */
    val readOnly: Boolean
        get() = mode === AccessMode.READ

    init {
        indices = getIndexFiles(base)
        data = getDataFile(base)
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

    /**
     * Decodes a file into an [Archive].
     */
    fun getArchive(type: Int, file: Int): Archive {
        return ArchiveCodec.decode(getFile(type, file))
    }

    /**
     * Gets a file.
     */
    fun getFile(descriptor: FileDescriptor): ByteBuffer {
        val (size, block) = getIndex(descriptor)
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

            require(id == currentChunk) {
                "Chunk id mismatch: id=$id, chunk=$currentChunk, type=${descriptor.type}, file=${descriptor.file}."
            }
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
                Preconditions.checkArgument(nextType == descriptor.type + 1, "File type mismatch.")
                Preconditions.checkArgument(nextFile == descriptor.file, "File id mismatch.")
            }
        }

        return buffer.apply { flip() }
    }

    /**
     * Gets a file.
     */
    fun getFile(type: Int, file: Int): ByteBuffer {
        return getFile(FileDescriptor(type, file))
    }

    /**
     * Gets the number of files with the specified type.
     */
    fun getFileCount(type: Int): Int {
        Preconditions.checkElementIndex(type, indices.size, "File type out of bounds.")

        val index = indices[type]
        synchronized(index) {
            return (index.length() / Index.BYTES).toInt()
        }
    }

    /**
     * Gets the index of a file.
     */
    fun getIndex(descriptor: FileDescriptor): Index {
        val type = descriptor.type
        Preconditions.checkElementIndex(type, indices.size, "File descriptor type out of bounds.")

        val index = indices[type]
        val position = (descriptor.file * Index.BYTES).toLong()
        require(position >= 0 && index.length() >= position + Index.BYTES) { "Could not find find index." }

        val buffer = ByteBuffer.allocate(Index.BYTES)
        synchronized(index) {
            index.seek(position)
            index.readFully(buffer.array())
        }

        return IndexCodec.decode(buffer)
    }

    /**
     * Gets the data file (`main_file_cache.dat`), as a [RandomAccessFile].
     */
    private fun getDataFile(base: Path): RandomAccessFile {
        val resources = base.resolve("main_file_cache.dat")

        if (!Files.exists(resources) || Files.isDirectory(resources)) {
            throw FileNotFoundException("No data file present.")
        }

        return RandomAccessFile(resources.toFile(), mode.asUnix())
    }

    /**
     * Gets the index files, as a [List] of [RandomAccessFile]s.
     */
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
