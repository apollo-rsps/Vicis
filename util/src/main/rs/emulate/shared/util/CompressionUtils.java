package rs.emulate.shared.util;

import com.google.common.io.ByteStreams;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A class that contains methods to compress and decompress BZIP2 and GZIP byte arrays.
 *
 * @author Graham
 * @author Major
 * @author Ryley
 */
public final class CompressionUtils {

	/**
	 * Decompresses the BZIP2 data stored in the specified {@link ByteBuffer}.
	 *
	 * @param buffer The DataBuffer.
	 * @return A DataBuffer containing the decompressed data.
	 * @throws IOException If there is an error decompressing the data.
	 */
	public static DataBuffer bunzip2(DataBuffer buffer) throws IOException {
		return DataBuffer.wrap(bunzip2(buffer.getRemainingBytes()));
	}

	/**
	 * Decompresses a BZIP2 file.
	 *
	 * @param bytes The compressed bytes without the header.
	 * @return The decompressed bytes.
	 * @throws IOException if an I/O error occurs.
	 */
	public static byte[] bunzip2(byte[] bytes) throws IOException {
		byte[] bzip2 = new byte[bytes.length + 4];
		bzip2[0] = 'B';
		bzip2[1] = 'Z';
		bzip2[2] = 'h';
		bzip2[3] = '1';
		System.arraycopy(bytes, 0, bzip2, 4, bytes.length);

		return ByteStreams.toByteArray(new BZip2CompressorInputStream(new ByteArrayInputStream(bzip2)));
	}

	/**
	 * BZIP2s the data stored in the specified {@link DataBuffer}.
	 *
	 * @param buffer The DataBuffer.
	 * @return A DataBuffer containing the compressed data, without the BZIP2 header.
	 * @throws IOException If there is an error compressing the data.
	 */
	public static DataBuffer bzip2(DataBuffer buffer) throws IOException {
		return DataBuffer.wrap(bzip2(buffer.getRemainingBytes()));
	}

	/**
	 * Bzip2s the specified array, removing the header.
	 *
	 * @param uncompressed The uncompressed array.
	 * @return The compressed array.
	 * @throws IOException If there is an error compressing the array.
	 */
	public static byte[] bzip2(byte[] uncompressed) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (BZip2CompressorOutputStream compressor = new BZip2CompressorOutputStream(out, 1)) {
			compressor.write(uncompressed);
			compressor.finish();

			byte[] compressed = out.toByteArray();
			byte[] stripped = new byte[compressed.length - 4]; // Strip the header
			System.arraycopy(compressed, 4, stripped, 0, stripped.length);
			return stripped;
		}
	}

	/**
	 * Decompresses the GZIP data stored in the specified {@link DataBuffer}.
	 *
	 * @param buffer The DataBuffer.
	 * @return A DataBuffer containing the decompressed data.
	 * @throws IOException If there is an error decompressing the data.
	 */
	public static DataBuffer gunzip(DataBuffer buffer) throws IOException {
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		try (InputStream decompressor = new GZIPInputStream(new ByteArrayInputStream(data));
		     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			while (true) {
				byte[] buf = new byte[1024];
				int read = decompressor.read(buf, 0, buf.length);
				if (read == -1) {
					break;
				}

				out.write(buf, 0, read);
			}

			return DataBuffer.wrap(out.toByteArray());
		}
	}

	/**
	 * Gzips the data stored in the specified {@link DataBuffer}.
	 *
	 * @param buffer The DataBuffer.
	 * @return A DataBuffer containing the compressed data.
	 * @throws IOException If there is an error compressing the data.
	 */
	public static DataBuffer gzip(DataBuffer buffer) throws IOException {
		byte[] data = buffer.getRemainingBytes();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (DeflaterOutputStream compressor = new GZIPOutputStream(out)) {
			compressor.write(data);
			compressor.finish();
		}

		return DataBuffer.wrap(out.toByteArray());
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private CompressionUtils() {

	}

}