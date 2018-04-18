package rs.emulate.legacy

import com.google.common.base.Preconditions
import rs.emulate.shared.util.DataBuffer

/**
 * Contains methods for encoding and decoding [Index] objects.
 */
internal object IndexCodec {

    /**
     * Decodes the [DataBuffer] into an index.
     * @throws IllegalArgumentException If the buffer length is invalid.
     */
    fun decode(buffer: DataBuffer): Index {
        Preconditions.checkArgument(buffer.remaining() == Index.BYTES, "Incorrect buffer length.")

        val size = buffer.getUnsignedTriByte()
        val block = buffer.getUnsignedTriByte()

        return Index(size, block)
    }

    /**
     * Encodes the index into a [DataBuffer].
     */
    fun encode(index: Index): DataBuffer {
        return DataBuffer.allocate(6).putTriByte(index.size).putTriByte(index.block).flip()
    }

}
