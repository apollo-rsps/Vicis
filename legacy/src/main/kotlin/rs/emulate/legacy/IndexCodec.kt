package rs.emulate.legacy

import com.google.common.base.Preconditions
import rs.emulate.util.getUnsignedTriByte
import rs.emulate.util.putTriByte
import java.nio.ByteBuffer

/**
 * Contains methods for encoding and decoding [Index] objects.
 */
internal object IndexCodec {

    /**
     * Decodes the [ByteBuffer] into an index.
     * @throws IllegalArgumentException If the buffer length is invalid.
     */
    fun decode(buffer: ByteBuffer): Index {
        Preconditions.checkArgument(buffer.remaining() == Index.BYTES, "Incorrect buffer length.")

        val size = buffer.getUnsignedTriByte()
        val block = buffer.getUnsignedTriByte()

        return Index(size, block)
    }

    /**
     * Encodes the index into a [ByteBuffer].
     */
    fun encode(index: Index): ByteBuffer {
        return ByteBuffer.allocate(6).putTriByte(index.size).putTriByte(index.block).apply { flip() }
    }

}
