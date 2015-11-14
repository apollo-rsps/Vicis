package rs.emulate.legacy.archive;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link ArchiveUtils#hash} function.
 *
 * @author Major
 */
public final class ArchiveHashTest {

	/**
	 * The expected hash of the {@link #TEST_NAME} string.
	 */
	private static final int TEST_HASH = 11_943_852;

	/**
	 * The string to hash.
	 */
	private static final String TEST_NAME = "test";

	/**
	 * Executes the test.
	 */
	@Test
	public void test() {
		int hash = ArchiveUtils.hash(TEST_NAME);
		assertEquals(TEST_HASH, hash);
	}

}