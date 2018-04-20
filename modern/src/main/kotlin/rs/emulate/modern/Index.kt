package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer

import java.nio.ByteBuffer

/**
 * An [Index] points to a file inside a [FileStore].
 *
 * @param size The size of the file in bytes.
 * @param sector The number of the first sector that contains the file.
 */
class Index(val size: Int, val sector: Int) {

    /**
     * Encodes this index into a byte buffer.
     */
    fun encode(): DataBuffer {
        return DataBuffer.allocate(Index.SIZE).apply {
            putTriByte(size)
            putTriByte(sector)

            flip()
        }
    }

    companion object {

        /**
         * The size of an index, in bytes.
         */
        const val SIZE = 6

        /**
         * Decodes the specified [ByteBuffer] into an [Index] object.
         */
        fun decode(buffer: DataBuffer): Index {
            require(buffer.remaining() == SIZE) { "Buffer size must contain $SIZE bytes." }

            val size = buffer.getUnsignedTriByte()
            val sector = buffer.getUnsignedTriByte()
            return Index(size, sector)
        }
    }

}
