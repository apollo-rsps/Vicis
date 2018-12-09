package rs.emulate.modern.compression

import com.google.common.io.ByteStreams
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream

fun ByteBuf.unlzma(uncompressedLength: Int): ByteBuf {
    val header = Unpooled.wrappedBuffer(Bzip.HEADER)
    val input = Unpooled.wrappedBuffer(header, this)

    val output = Unpooled.buffer(uncompressedLength, uncompressedLength)

    LZMACompressorInputStream(ByteBufInputStream(input)).use { inputStream ->
        ByteBufOutputStream(output).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    return output.asReadOnly()
}

fun ByteBuf.lzma(): ByteBuf {
    val output = Unpooled.buffer()

    ByteBufInputStream(this).use { inputStream ->
        LZMACompressorOutputStream(ByteBufOutputStream(output)).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    return output.asReadOnly()
}
