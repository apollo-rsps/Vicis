package rs.emulate.legacy

import com.google.common.base.Preconditions
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveCodec
import rs.emulate.legacy.archive.CompressionType
import rs.emulate.shared.util.DataBuffer

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.ArrayList
import java.util.HashMap
import kotlin.collections.MutableMap.MutableEntry

/**
 * A container for a set of [Archive]s.
 */
class Cache {

    /**
     * The Map of FileDescriptors to Buffers.
     */
    private val files = HashMap<FileDescriptor, DataBuffer>()

    /**
     * Encodes this cache, writing the output to the specified files in the specified [Path].
     *
     * @param base The directory to place the output files in.
     * @param dataFile The name of the data file.
     * @param indexPrefix The prefix of the index files.
     * @throws IOException If there is an error writing the data to disk.
     */
    fun encode(base: Path, dataFile: String = DEFAULT_DATA_FILE_NAME, indexPrefix: String = DEFAULT_INDEX_FILE_PREFIX) {
        if (Files.notExists(base)) {
            Files.createDirectories(base)
        } else if (!Files.isDirectory(base)) {
            throw IllegalArgumentException("Specified base path ($base) must be a directory.")
        }

        val options = arrayOf<OpenOption>(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE)
        val dataPath = base.resolve(dataFile)

        FileChannel.open(dataPath, *options).use { data ->
            data.write(ByteBuffer.allocate(520)) // write the empty block
            val indices = HashMap<Int, FileChannel>()

            val zero = FileChannel.open(base.resolve(indexPrefix + '0'), *options)
            indices[0] = zero

            zero.write(IndexCodec.encode(Index(0, 0)).byteBuffer) // Index for the empty block.
            var block = 1

            for (entry in sortedFileList()) {
                val descriptor = entry.key
                val buffer = entry.value

                val size = buffer.remaining()
                val type = descriptor.type
                val blocks = (size + FileSystemConstants.CHUNK_SIZE - 1) / FileSystemConstants.CHUNK_SIZE

                var index: FileChannel? = indices[type]

                if (index == null) {
                    index = FileChannel.open(base.resolve(indexPrefix + type), *options)
                    indices[type] = index
                }

                val pointer = IndexCodec.encode(Index(size, block))
                index!!.write(pointer.byteBuffer)

                val file = descriptor.file
                for (id in 0 until blocks) {
                    val header = DataBuffer.allocate(FileSystemConstants.HEADER_SIZE)
                    header.putShort(file).putShort(id)
                    header.putTriByte(++block).putByte(type + 1).flip()
                    data.write(header.byteBuffer)

                    val remaining = buffer.remaining()
                    if (remaining < FileSystemConstants.CHUNK_SIZE) {
                        data.write(buffer.byteBuffer)
                        val padding = FileSystemConstants.CHUNK_SIZE - remaining

                        data.write(ByteBuffer.allocate(padding))
                    } else {
                        val chunk = DataBuffer.allocate(FileSystemConstants.CHUNK_SIZE)
                        chunk.fill(buffer)

                        data.write(chunk.byteBuffer)
                    }
                }
            }

            for (index in indices.values) {
                index.close()
            }
        }
    }

    /**
     * Gets the [Archive] with the specified [FileDescriptor].
     *
     * @param descriptor The descriptor.
     * @return The Archive.
     */
    fun getArchive(descriptor: FileDescriptor): Archive {
        val file = files[descriptor]

        if (file != null) {
            try {
                return ArchiveCodec.decode(file)
            } catch (e: IOException) {
                throw IllegalArgumentException("Cannot decode a regular file into an archive.", e)
            }

        }

        throw IllegalArgumentException("No archive with the specified descriptor is stored in this Cache.")
    }

    /**
     * Gets the file data with the specified [FileDescriptor].
     */
    fun getFile(descriptor: FileDescriptor): DataBuffer {
        return files[descriptor]!!
    }

    /**
     * Places an [Archive] with the specified [FileDescriptor] into this Cache.
     *
     * @param descriptor The FileDescriptor of the Archive. Must not be `null`.
     * @param archive The Archive. Must not be `null`.
     * @param type The [CompressionType] to apply to the Archive. Must not be `null`.
     */
    fun putArchive(descriptor: FileDescriptor, archive: Archive, type: CompressionType) {
        try {
            files[descriptor] = ArchiveCodec.encode(archive, type)
        } catch (e: IOException) {
            throw IllegalArgumentException("Invalid archive provided.", e)
        }

    }

    /**
     * Places the file with the specified [FileDescriptor] into this Cache.
     *
     * @param descriptor The FileDescriptor.
     * @param file The file data.
     */
    fun putFile(descriptor: FileDescriptor, file: DataBuffer) {
        files[descriptor] = file
    }

    /**
     * Replaces the [Archive] represented by the specified [FileDescriptor] with the specified Archive.
     *
     * @param descriptor The FileDescriptor of the Archive. Must not be `null`.
     * @param archive The Archive to replace the existing one with. Must not be `null`.
     * @param type The [CompressionType] to apply to the new Archive. Must not be `null`.
     * @throws IllegalArgumentException If the Archive does not exist.
     */
    fun replaceArchive(descriptor: FileDescriptor, archive: Archive, type: CompressionType) {
        val buffer = files[descriptor]
        Preconditions.checkArgument(buffer != null,
            "Archive does not exist, cannot replace it (use putArchive() instead).")

        putArchive(descriptor, archive, type)
    }

    /**
     * Gets the [List] of files to encode, in ascending order.
     */
    private fun sortedFileList(): List<MutableEntry<FileDescriptor, DataBuffer>> {
        val comparator: Comparator<MutableEntry<FileDescriptor, DataBuffer>> = Comparator { (first), (second) ->
            val type = Integer.compare(first.type, second.type)

            if (type == 0) Integer.compare(first.file, second.file) else type
        }

        return ArrayList<MutableEntry<FileDescriptor, DataBuffer>>(files.entries).apply { sortWith(comparator) }
    }

    companion object {

        /**
         * The default name of the data file.
         */
        private const val DEFAULT_DATA_FILE_NAME = "main_file_cache.dat"

        /**
         * The default prefix of an index file.
         */
        private const val DEFAULT_INDEX_FILE_PREFIX = "main_file_cache.idx"
    }

}
