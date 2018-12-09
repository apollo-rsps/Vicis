package rs.emulate.modern

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.util.crc32

class VersionedContainer(private var buffer: ByteBuf, version: Int) {
    val checksum: Int
        get() = buffer.crc32()

    var version = version
        set(value) {
            value and 0xFFFF
        }

    /* returns an immutable buffer */
    fun getBuffer(): ByteBuf {
        return buffer.slice()
    }

    /* accepts an immutable buffer */
    fun setBuffer(buffer: ByteBuf) {
        this.buffer = buffer
    }

    /* returns an immutable buffer */
    fun write(): ByteBuf {
        val trailer = Unpooled.buffer(2, 2)
        trailer.writeShort(version)
        return Unpooled.wrappedBuffer(buffer, trailer).asReadOnly()
    }

    companion object {

        /* accepts an immutable buffer */
        fun ByteBuf.readVersionedContainer(): VersionedContainer {
            require(readableBytes() >= 2) { "No version trailer" }

            val data = readSlice(readableBytes() - 2)
            val version = readUnsignedShort()

            return VersionedContainer(data, version)
        }
    }
}
