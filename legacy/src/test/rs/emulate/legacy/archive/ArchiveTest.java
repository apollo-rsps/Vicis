package rs.emulate.legacy.archive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import rs.emulate.shared.util.DataBuffer;

/**
 * Tests that the {@link ArchiveCodec#encode} and {@link ArchiveCodec#decode} methods are functioning correctly.
 * 
 * @author Major
 */
public final class ArchiveTest {

	/**
	 * The buffer used for testing, containing 15 bytes.
	 */
	private static final DataBuffer TEST_BUFFER = DataBuffer.wrap(
			new byte[] { 32, 78, 107, -30, 29, -81, -49, 113, 117, 51, 26, -30, -96, -34, 68 }).asReadOnlyBuffer();

	/**
	 * The archive entry identifier used in the test.
	 */
	private static final int TEST_IDENTIFIER = ArchiveUtils.hash("test");

	/**
	 * The ArchiveEntry to test.
	 */
	private static final ArchiveEntry ENTRY = new ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER);

	/**
	 * Executes the test.
	 * 
	 * @throws IOException If there is an error encoding or decoding the archive.
	 */
	@Test
	public void test() throws IOException {
		Archive archive = new Archive(Arrays.asList(ENTRY));

		DataBuffer encoded = ArchiveCodec.encode(archive, CompressionType.ARCHIVE_COMPRESSION);
		Archive decoded = ArchiveCodec.decode(encoded);

		assertEquals(archive, decoded);

		encoded = ArchiveCodec.encode(archive, CompressionType.ENTRY_COMPRESSION);
		decoded = ArchiveCodec.decode(encoded);

		assertEquals(archive, decoded);
	}

}