package rs.emulate.shared.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import com.google.common.io.ByteStreams;

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
	 * @param buffer The buffer.
	 * @return A new byte buffer containing the decompressed data.
	 * @throws IOException If there is an error decompressing the data.
	 */
	public static DataBuffer bunzip2(DataBuffer buffer) throws IOException {
		return DataBuffer.wrap(bunzip2(buffer.getBytes()));
	}

	/**
	 * Uncompresses a BZIP2 file.
	 * 
	 * @param bytes The compressed bytes without the header.
	 * @return The uncompressed bytes.
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
	 * Bzip2s the data stored in the specified {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @return A new byte buffer containing the compressed data, without the bzip2 header.
	 * @throws IOException If there is an error compressing the data.
	 */
	public static DataBuffer bzip2(DataBuffer buffer) throws IOException {
		return DataBuffer.wrap(bzip2(buffer.getBytes()));
	}

	/**
	 * Compresses a BZIP2 file.
	 * 
	 * @param bytes The uncompressed bytes.
	 * @return The compressed bytes without the header.
	 * @throws IOException if an I/O error occurs.
	 */
	public static byte[] bzip2(byte[] bytes) throws IOException {
		/* try-with-resources not necessary as closing has no effect */
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ByteStreams.copy(new ByteArrayInputStream(bytes), new BZip2CompressorOutputStream(bout, 1));

		/* strip the header from the byte array and return it */
		bytes = bout.toByteArray();
		byte[] bzip2 = new byte[bytes.length - 4];
		System.arraycopy(bytes, 4, bzip2, 0, bzip2.length);
		return bzip2;
	}

	/**
	 * Decompresses the GZIP data stored in the specified {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @return A new byte buffer containing the decompressed data.
	 * @throws IOException If there is an error decompressing the data.
	 */
	public static DataBuffer gunzip(DataBuffer buffer) throws IOException {
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		return DataBuffer.wrap(ByteStreams.toByteArray(new GZIPInputStream(new ByteArrayInputStream(data))));
	}

	/**
	 * Gzips the data stored in the specified {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @return A new byte buffer, containing the compressed data.
	 * @throws IOException If there is an error compressing the data.
	 */
	public static DataBuffer gzip(DataBuffer buffer) throws IOException {
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		/* try-with-resources not necessary as closing has no effect */
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ByteStreams.copy(new ByteArrayInputStream(data), new GZIPOutputStream(bout));

		return DataBuffer.wrap(bout.toByteArray());
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private CompressionUtils() {

	}

}