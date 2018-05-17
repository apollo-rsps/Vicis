package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.FileChannelUtils

import java.io.Closeable
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.ArrayList

/**
 * A file store holds multiple files inside a "virtual" file system made up of several index files and a single data
 * file.
 *
 * @param dataChannel The data file.
 * @param indexChannels The index files.
 * @param metaChannel The 'meta' index file.
 */
class FileStore(
    private val dataChannel: FileChannel,
    private val indexChannels: Array<FileChannel>,
    private val metaChannel: FileChannel
) : Closeable {

    /**
     * Gets the number of index files, not including the meta index file.
     */
    val typeCount: Int
        get() = indexChannels.size

    override fun close() {
        dataChannel.close()
        indexChannels.forEach { channel -> channel.close() }
        metaChannel.close()
    }

    /**
     * Gets the number of files of the specified type.
     */
    fun getFileCount(type: Int): Int {
        if ((type < 0 || type >= indexChannels.size) && type != 255) {
            throw IllegalArgumentException("Type must be within 0-${indexChannels.size} or 255.")
        }

        return (if (type == 255) metaChannel.size() else indexChannels[type].size()).toInt() / Index.SIZE
    }

    /**
     * Reads a file.
     */
    fun read(type: Int, id: Int): DataBuffer {
        if ((type < 0 || type >= indexChannels.size) && type != 255) {
            throw FileNotFoundException("Specified type is invalid.")
        }

        val channel = if (type == 255) metaChannel else indexChannels[type]
        var position = (id * Index.SIZE).toLong()
        if (position < 0 || position >= channel.size()) {
            channel.close()
            throw FileNotFoundException("Position to read from is invalid.")
        }

        var buffer = DataBuffer.allocate(Index.SIZE)
        FileChannelUtils.readFully(channel, buffer, position)

        val index = Index.decode(buffer.flip())

        val data = DataBuffer.allocate(index.size)
        buffer = DataBuffer.allocate(Sector.SIZE)

        var chunk = 0
        var remaining = index.size
        position = (index.sector * Sector.SIZE).toLong()

        do {
            buffer.clear()
            FileChannelUtils.readFully(dataChannel, buffer, position)
            val sector = Sector.decode(buffer.flip())

            if (remaining > Sector.DATA_SIZE) {
                data.put(sector.data.array(), 0, Sector.DATA_SIZE)
                remaining -= Sector.DATA_SIZE

                when {
                    sector.type != type -> throw IOException("File type mismatch.")
                    sector.id != id -> throw IOException("File id mismatch.")
                    sector.chunk != chunk++ -> throw IOException("Chunk mismatch.")
                }

                position = (sector.nextSector * Sector.SIZE).toLong()
            } else {
                data.put(sector.data.array(), 0, remaining)
                remaining = 0
            }
        } while (remaining > 0)

        return data.flip()
    }

    /**
     * Writes a file.
     */
    fun write(type: Int, id: Int, buffer: DataBuffer) {
        write(type, id, buffer.byteBuffer)
    }

    /**
     * Writes a file.
     */
    fun write(type: Int, id: Int, data: ByteBuffer) {
        data.mark()

        if (!write(type, id, data, true)) {
            data.reset()
            write(type, id, data, false)
        }
    }

    /**
     * Writes a file.
     *
     * @param type The type of the file.
     * @param id The id of the file.
     * @param data A [ByteBuffer] containing the contents of the file.
     * @param overwrite A flag indicating if the existing file should be overwritten.
     * @return Whether or not the file was written successfully.
     */
    private fun write(type: Int, id: Int, data: ByteBuffer, overwrite: Boolean): Boolean {
        var overwrite = overwrite
        if ((type < 0 || type >= indexChannels.size) && type != 255) {
            throw FileNotFoundException("Specified type is invalid.")
        }

        (if (type == 255) metaChannel else indexChannels[type]).use { indexChannel ->
            var nextSector: Int
            var pointer = (id * Index.SIZE).toLong()
            if (overwrite) {
                if (pointer < 0) {
                    throw IOException("Pointer < 0.")
                } else if (pointer >= indexChannel.size()) {
                    return false
                }

                val buffer = DataBuffer.allocate(Index.SIZE)
                FileChannelUtils.readFully(indexChannel, buffer, pointer)

                val index = Index.decode(buffer.flip())
                nextSector = index.sector
                if (nextSector <= 0 || nextSector > dataChannel.size() * Sector.SIZE) {
                    return false
                }
            } else {
                nextSector = (dataChannel.size() + Sector.SIZE - 1).toInt() / Sector.SIZE
                if (nextSector == 0) {
                    nextSector = 1
                }
            }

            val index = Index(data.remaining(), nextSector)
            indexChannel.write(index.encode().byteBuffer, pointer)

            val buf = DataBuffer.allocate(Sector.SIZE)

            var chunk = 0
            var remaining = index.size
            do {
                val currentSector = nextSector
                pointer = (currentSector * Sector.SIZE).toLong()
                nextSector = 0

                if (overwrite) {
                    buf.clear()
                    FileChannelUtils.readFully(dataChannel, buf, pointer)
                    val sector = Sector.decode(buf.flip())

                    if (sector.type != type) {
                        return false
                    } else if (sector.id != id) {
                        return false
                    } else if (sector.chunk != chunk) {
                        return false
                    }

                    nextSector = sector.nextSector
                    if (nextSector < 0 || nextSector > dataChannel.size() / Sector.SIZE) {
                        return false
                    }
                }

                if (nextSector == 0) {
                    overwrite = false
                    nextSector = ((dataChannel.size() + Sector.SIZE - 1) / Sector.SIZE).toInt()

                    if (nextSector == 0) {
                        nextSector++
                    }
                    if (nextSector == currentSector) {
                        nextSector++
                    }
                }

                val bytes = ByteArray(Sector.DATA_SIZE)
                if (remaining < Sector.DATA_SIZE) {
                    data.get(bytes, 0, remaining)
                    nextSector = 0 // mark as EOF
                    remaining = 0
                } else {
                    remaining -= Sector.DATA_SIZE
                    data.get(bytes, 0, Sector.DATA_SIZE)
                }

                val sector = Sector(type, id, chunk++, nextSector, DataBuffer.wrap(bytes))
                dataChannel.write(sector.encode().byteBuffer, pointer)
            } while (remaining > 0)
        }

        return true
    }

    companion object {

        /**
         * Creates a new file store.
         * @param indices The number of indices.
         */
        fun create(root: Path, indices: Int): FileStore {
            try {
                Files.createDirectories(root)

                for (index in 0 until indices) {
                    val file = root.resolve("main_file_cache.idx$index")
                    Files.createDirectory(file)
                }

                val meta = root.resolve("main_file_cache.idx255")
                Files.createDirectory(meta)

                val data = root.resolve("main_file_cache.dat2")
                Files.createDirectory(data)
            } catch (e: IOException) {
                throw IOException("Could not make directories.", e)
            }

            return open(root)
        }

        /**
         * Creates a new file store.
         */
        fun create(root: String, indices: Int): FileStore {
            return create(Paths.get(root), indices)
        }

        /**
         * Opens the file store stored in the specified directory.
         */
        fun open(root: Path): FileStore {
            val main = root.resolve("main_file_cache.dat2")
            if (!Files.exists(main)) {
                throw FileNotFoundException("Main file cache does not exist in $root.")
            }

            val data = FileChannel.open(main, StandardOpenOption.READ, StandardOpenOption.WRITE)

            val indices = ArrayList<FileChannel>()
            for (i in 0..253) {
                val index = root.resolve("main_file_cache.idx$i")
                if (!Files.exists(index)) {
                    break
                }

                val channel = FileChannel.open(index, StandardOpenOption.READ, StandardOpenOption.WRITE)
                indices.add(channel)
            }

            if (indices.isEmpty()) {
                throw FileNotFoundException("Index file does not exist.")
            }

            val reference = root.resolve("main_file_cache.idx255")
            if (!Files.exists(reference)) {
                throw FileNotFoundException("Index 255 does not exist.")
            }

            val meta = FileChannel.open(reference, StandardOpenOption.READ, StandardOpenOption.WRITE)
            return FileStore(data, indices.toTypedArray(), meta)
        }

        /**
         * Opens the file store stored in the specified directory.
         */
        fun open(root: String): FileStore {
            return open(Paths.get(root))
        }
    }

}
