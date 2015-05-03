package rs.emulate.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import rs.emulate.shared.util.CompressionUtils;
import rs.emulate.shared.util.DataBuffer;

/**
 * A test for the {@link CompressionUtils} class.
 * 
 * @author Graham
 */
public class TestCompressionUtil {

	/**
	 * Tests the {@link CompressionUtils#gzip} and {@link CompressionUtils#gunzip} methods.
	 * 
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testGzip() throws IOException {
		String test = "Hello, World!";
		DataBuffer data = DataBuffer.wrap(test.getBytes());
		DataBuffer compressed = CompressionUtils.gzip(data);
		DataBuffer decompressed = CompressionUtils.gunzip(compressed);

		assertEquals(test, new String(decompressed.array()));
	}

	/**
	 * Tests the {@link CompressionUtils#bzip2} and {@link CompressionUtils#bunzip2} methods.
	 * 
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testBzip2() throws IOException {
		String str = "Hello, World!";
		byte[] data = str.getBytes();
		byte[] compressed = CompressionUtils.bzip2(data);
		CompressionUtils.bunzip2(compressed);
		assertEquals(str, new String(data));
	}

}