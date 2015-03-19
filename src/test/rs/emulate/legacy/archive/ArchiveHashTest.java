package rs.emulate.legacy.archive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link ArchiveUtils#hash} function.
 * 
 * @author Major
 */
public final class ArchiveHashTest {

	/**
	 * The string to hash.
	 */
	private static final String TEST_NAME = "test";

	/**
	 * The expected hash of the {@link #TEST_NAME} string.
	 */
	private static final int TEST_HASH = 11_943_852;

	/**
	 * Executes the test.
	 */
	@Test
	public void test() {
		int hash = ArchiveUtils.hash(TEST_NAME);
		assertEquals(TEST_HASH, hash);
	}

}