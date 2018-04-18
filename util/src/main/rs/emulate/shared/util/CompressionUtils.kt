package rs.emulate.shared.util

import com.google.common.io.ByteStreams
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * A class that contains methods to compress and decompress BZIP2 and GZIP byte arrays.
 */
object CompressionUtils {

    /**
     * Decompresses the BZIP2 data stored in the specified [ByteBuffer].
     *
     * @param buffer The DataBuffer.
     * @return A DataBuffer containing the decompressed data.
     * @throws IOException If there is an error decompressing the data.
     */
    fun bunzip2(buffer: DataBuffer): DataBuffer {
        return DataBuffer.wrap(bunzip2(buffer.remainingBytes))
    }

    /**
     * Decompresses a BZIP2 file.
     *
     * @param bytes The compressed bytes without the header.
     * @return The decompressed bytes.
     * @throws IOException if an I/O error occurs.
     */
    fun bunzip2(bytes: ByteArray): ByteArray {
        val bzip2 = ByteArray(bytes.size + 4)
        bzip2[0] = 'B'.toByte()
        bzip2[1] = 'Z'.toByte()
        bzip2[2] = 'h'.toByte()
        bzip2[3] = '1'.toByte()
        System.arraycopy(bytes, 0, bzip2, 4, bytes.size)

        return ByteStreams.toByteArray(BZip2CompressorInputStream(ByteArrayInputStream(bzip2)))
    }

    /**
     * BZIP2s the data stored in the specified [DataBuffer].
     *
     * @param buffer The DataBuffer.
     * @return A DataBuffer containing the compressed data, without the BZIP2 header.
     * @throws IOException If there is an error compressing the data.
     */
    fun bzip2(buffer: DataBuffer): DataBuffer {
        return DataBuffer.wrap(bzip2(buffer.remainingBytes))
    }

    /**
     * Bzip2s the specified array, removing the header.
     *
     * @param uncompressed The uncompressed array.
     * @return The compressed array.
     * @throws IOException If there is an error compressing the array.
     */
    fun bzip2(uncompressed: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        BZip2CompressorOutputStream(out, 1).use { compressor ->
            compressor.write(uncompressed)
            compressor.finish()

            val compressed = out.toByteArray()
            val stripped = ByteArray(compressed.size - 4) // Strip the header
            System.arraycopy(compressed, 4, stripped, 0, stripped.size)
            return stripped
        }
    }

    /**
     * Decompresses the GZIP data stored in the specified [DataBuffer].
     *
     * @param buffer The DataBuffer.
     * @return A DataBuffer containing the decompressed data.
     * @throws IOException If there is an error decompressing the data.
     */
    fun gunzip(buffer: DataBuffer): DataBuffer {
        val data = ByteArray(buffer.remaining())
        buffer.read(data)

        GZIPInputStream(ByteArrayInputStream(data)).use { decompressor ->
            ByteArrayOutputStream().use { out ->
                while (true) {
                    val buf = ByteArray(1024)
                    val read = decompressor.read(buf, 0, buf.size)
                    if (read == -1) {
                        break
                    }

                    out.write(buf, 0, read)
                }

                return DataBuffer.wrap(out.toByteArray())
            }
        }
    }

    /**
     * Gzips the data stored in the specified [DataBuffer].
     *
     * @param buffer The DataBuffer.
     * @return A DataBuffer containing the compressed data.
     * @throws IOException If there is an error compressing the data.
     */
    fun gzip(buffer: DataBuffer): DataBuffer {
        val data = buffer.remainingBytes

        val out = ByteArrayOutputStream()
        GZIPOutputStream(out).use { compressor ->
            compressor.write(data)
            compressor.finish()
        }

        return DataBuffer.wrap(out.toByteArray())
    }

}
