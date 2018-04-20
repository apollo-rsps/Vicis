package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer
import java.nio.ByteBuffer

/**
 * A [Sector] contains a header and data. The header contains information used to verify the integrity of the
 * cache like the current file id, type and chunk. It also contains a pointer to the next sector such that the sectors
 * form a singly-linked list. The data is simply up to 512 bytes of the file.
 *
 * @param type The type of the file.
 * @param id The file's id.
 * @param chunk The chunk of the file this sector contains.
 * @param nextSector The sector containing the next chunk.
 * @param data The data in this sector.
 */
class Sector(val type: Int, val id: Int, val chunk: Int, val nextSector: Int, val data: DataBuffer) {

    /**
     * Encodes this sector into a [ByteBuffer].
     */
    fun encode(): DataBuffer {
        return DataBuffer.allocate(SIZE).apply {
            putShort(id.toShort().toInt())
            putShort(chunk.toShort().toInt())
            putTriByte(nextSector)
            putByte(type.toByte().toInt())
            put(data)

            flip()
        }
    }

    companion object {

        /**
         * The size of the data within a sector in bytes.
         */
        const val DATA_SIZE = 512

        /**
         * The size of the header within a sector in bytes.
         */
        const val HEADER_SIZE = 8

        /**
         * The total size of a sector in bytes.
         */
        const val SIZE = HEADER_SIZE + DATA_SIZE

        /**
         * Decodes the specified [DataBuffer] into a [Sector] object.
         */
        fun decode(buffer: DataBuffer): Sector {
            require(buffer.remaining() == SIZE) { "Buffer must have $SIZE bytes remaining." }

            val id = buffer.getUnsignedShort()
            val chunk = buffer.getUnsignedShort()
            val nextSector = buffer.getUnsignedTriByte()
            val type = buffer.getUnsignedByte()

            val data = DataBuffer.allocate(DATA_SIZE)
            data.fill(buffer)

            return Sector(type, id, chunk, nextSector, data)
        }
    }

}
