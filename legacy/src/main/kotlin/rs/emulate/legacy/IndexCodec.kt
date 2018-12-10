package rs.emulate.legacy

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.util.readUnsignedTriByte
import rs.emulate.util.writeTriByte
import java.nio.ByteBuffer

/**
 * Contains methods for encoding and decoding [Index] objects.
 */
internal object IndexCodec {

    /**
     * Decodes the [ByteBuffer] into an index.
     * @throws IllegalArgumentException If the buffer length is invalid.
     */
    fun decode(buffer: ByteBuf): Index {
        require(buffer.readableBytes() == Index.BYTES) { "Incorrect buffer length." }

        val size = buffer.readUnsignedTriByte()
        val block = buffer.readUnsignedTriByte()

        return Index(size, block)
    }

    /**
     * Encodes the index into a [ByteBuffer].
     */
    fun encode(index: Index): ByteBuf {
        return Unpooled.buffer(6).apply {
            writeTriByte(index.size)
            writeTriByte(index.block)
        }
    }

}
