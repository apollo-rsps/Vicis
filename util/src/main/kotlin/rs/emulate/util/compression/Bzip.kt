package rs.emulate.util.compression

import com.google.common.io.ByteStreams
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream

object Bzip {
    val HEADER = charArrayOf('B', 'Z', 'h', '1').map(Char::toByte).toByteArray()
}

fun ByteBuf.bunzip2(uncompressedLength: Int): ByteBuf {
    val header = Unpooled.wrappedBuffer(Bzip.HEADER)
    val input = Unpooled.wrappedBuffer(header, this)

    val output = Unpooled.buffer(uncompressedLength)

    BZip2CompressorInputStream(ByteBufInputStream(input)).use { inputStream ->
        ByteBufOutputStream(output).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    return output.asReadOnly()
}

fun ByteBuf.bzip2(): ByteBuf {
    val output = Unpooled.buffer()

    ByteBufInputStream(this).use { inputStream ->
        BZip2CompressorOutputStream(ByteBufOutputStream(output), 1).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    val length = output.readableBytes() - Bzip.HEADER.size
    return output.slice(Bzip.HEADER.size, length).asReadOnly()
}
