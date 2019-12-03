package rs.emulate.util.compression

import com.google.common.io.ByteStreams
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.Unpooled
import org.tukaani.xz.LZMA2Options
import org.tukaani.xz.LZMAInputStream
import org.tukaani.xz.LZMAOutputStream

fun ByteBuf.unlzma(uncompressedLength: Int): ByteBuf {
    val output = Unpooled.buffer(uncompressedLength, uncompressedLength)

    /*
     * The cache stores the uncompressed size in the container and avoids repeating this value in the LZMA header,
     * so read the mutated header ourselves and pass the values into the LZMA compressor directly.
     */

    val properties = readUnsignedByte().toByte()
    val dictSize = readUnsignedIntLE().toInt()

    LZMAInputStream(ByteBufInputStream(this), uncompressedLength.toLong(), properties, dictSize).use { inputStream ->
        ByteBufOutputStream(output).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    return output.asReadOnly()
}

fun ByteBuf.lzma(): ByteBuf {
    val output = Unpooled.buffer()
    val options = LZMA2Options() // use default preset

    val props = (options.pb * 5 + options.lp) * 9 + options.lc
    output.writeByte(props)

    repeat(4) {
        output.writeByte(options.dictSize ushr (8 * it) and 0xFF)
    }

    ByteBufInputStream(this).use { inputStream ->
        LZMAOutputStream(ByteBufOutputStream(output), options, false).use { outputStream ->
            ByteStreams.copy(inputStream, outputStream)
        }
    }

    return output.asReadOnly()
}
