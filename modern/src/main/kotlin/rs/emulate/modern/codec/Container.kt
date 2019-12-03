package rs.emulate.modern.codec

import com.google.common.collect.Iterators
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.util.compression.*
import rs.emulate.util.crypto.xtea.XteaKey
import rs.emulate.util.crypto.xtea.xteaDecrypt
import rs.emulate.util.crypto.xtea.xteaEncrypt

class Container(val compression: CompressionType, buffer: ByteBuf) {

    var buffer = buffer
        get() = field.slice()

    /* returns an immutable buffer */
    fun write(key: XteaKey = XteaKey.NONE): ByteBuf {
        val buf = Unpooled.buffer()

        buf.writeByte(compression.id)

        val data = when (compression) {
            CompressionType.NONE -> buffer.slice()
            CompressionType.BZIP2 -> buffer.slice().bzip2()
            CompressionType.GZIP -> buffer.slice().gzip()
            CompressionType.LZMA -> buffer.slice().lzma()
        }

        buf.writeInt(data.readableBytes())

        val start = buf.writerIndex()

        if (compression !== CompressionType.NONE) {
            buf.writeInt(buffer.readableBytes())
        }

        buf.writeBytes(data)

        if (!key.isZero) {
            val end = buf.writerIndex()
            buf.xteaEncrypt(start, end, key)
        }

        return buf.asReadOnly()
    }

    companion object {

        /* accepts any buffer, returns a Container that contains an immutable buffer */
        fun ByteBuf.readContainer(key: XteaKey = XteaKey.NONE): Container {
            var buf = this

            val compressionOrdinal = buf.readUnsignedByte().toInt()
            val compression = checkNotNull(CompressionType[compressionOrdinal]) {
                "Invalid compression type $compressionOrdinal."
            }

            val length = buf.readInt()
            var origBuf: ByteBuf? = null

            if (!key.isZero) {
                origBuf = buf
                buf = buf.copy()

                val start = buf.readerIndex()
                val end = if (compression === CompressionType.NONE) {
                    start + length
                } else {
                    start + length + Int.SIZE_BYTES
                }

                buf.xteaDecrypt(start, end, key)
            }

            val uncompressedLength = if (compression === CompressionType.NONE) {
                length
            } else {
                origBuf?.skipBytes(Int.SIZE_BYTES)
                buf.readInt()
            }

            var data = if (key.isZero && compression === CompressionType.NONE) {
                buf.readBytes(length)
            } else {
                /*
                 * If the container is encrypted, we can use readSlice() as we are already operating on a copy.
                 * If the container is compressed, we can use readSlice() as the decompression functions create a copy.
                 */
                buf.readSlice(length)
            }

            origBuf?.skipBytes(length)

            data = when (compression) {
                CompressionType.NONE -> data
                CompressionType.BZIP2 -> data.bunzip2(uncompressedLength)
                CompressionType.GZIP -> data.gunzip(uncompressedLength)
                CompressionType.LZMA -> data.unlzma(uncompressedLength)
            }

            return Container(compression, data.asReadOnly())
        }

        /* accepts any buffer, returns an immutable buffer */
        fun pack(buffer: ByteBuf, key: XteaKey = XteaKey.NONE): ByteBuf {
            val it = Iterators.forArray(*CompressionType.values())

            var type = it.next()
            var container = Container(type, buffer).write(key)

            while (it.hasNext()) {
                type = it.next()

                val tempContainer = Container(type, buffer).write(key)
                if (tempContainer.readableBytes() < container.readableBytes()) {
                    container = tempContainer
                }
            }

            return container
        }
    }
}
