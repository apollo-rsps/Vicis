package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

/**
 * An [Archive] is a file within the cache that can have multiple member files inside it.
 *
 * @param size The number of entries in the archive.
 */
class Archive(size: Int) {

    /**
     * The array of entries in this archive.
     */
    private val entries: Array<ByteBuffer?> = arrayOfNulls(size)

    /**
     * Encodes this [Archive] into a [ByteBuffer].
     *
     * Please note that this is a fairly simple implementation that does not attempt to use more than one chunk.
     */
    fun encode(): DataBuffer { // TODO: an implementation that can use more than one chunk
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            for (entry in entries.filterNotNull()) {
                val temp = ByteArray(entry.limit())
                entry.position(0)
                entry.get(temp)
                entry.position(0)

                os.write(temp)
            }

            var prev = 0
            for (entry in entries.filterNotNull()) {
                // since each file is stored in the only chunk, just write the delta-encoded file size
                val chunkSize = entry.limit()
                os.writeInt(chunkSize - prev)
                prev = chunkSize
            }

            bout.write(1)

            val bytes = bout.toByteArray()
            return DataBuffer.wrap(bytes)
        }
    }

    /**
     * Gets the entry with the specified id.
     */
    fun getEntry(id: Int): ByteBuffer? {
        return entries[id]
    }

    /**
     * Inserts/replaces the entry with the specified id.
     */
    fun putEntry(id: Int, buffer: ByteBuffer) {
        entries[id] = buffer
    }

    /**
     * Gets the size of this archive.
     * @return The size of this archive.
     */
    fun size(): Int {
        return entries.size
    }

    companion object {

        /**
         * Decodes the specified [ByteBuffer] into an archive.
         */
        fun decode(buffer: DataBuffer, size: Int): Archive {
            val archive = Archive(size)

            buffer.position(buffer.limit() - 1)
            val chunks = buffer.getByte() and 0xFF

            val chunkSizes = Array(chunks) { IntArray(size) }
            val sizes = IntArray(size)
            buffer.position(buffer.limit() - 1 - chunks * size * 4)

            for (chunk in 0 until chunks) {
                var chunkSize = 0

                for (id in 0 until size) {
                    val delta = buffer.getInt()
                    chunkSize += delta

                    chunkSizes[chunk][id] = chunkSize /* store the size of this chunk */
                    sizes[id] += chunkSize /* and add it to the size of the whole file */
                }
            }

            for (id in 0 until size) {
                archive.entries[id] = ByteBuffer.allocate(sizes[id])
            }

            buffer.position(0)
            for (chunk in 0 until chunks) {
                for (id in 0 until size) {
                    val chunkSize = chunkSizes[chunk][id]

                    val temp = ByteArray(chunkSize)
                    buffer.read(temp)

                    archive.entries[id]!!.put(temp)
                }
            }

            for (id in 0 until size) {
                archive.entries[id]!!.flip()
            }

            return archive
        }
    }

}
