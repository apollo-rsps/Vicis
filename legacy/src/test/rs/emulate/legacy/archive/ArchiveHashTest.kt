package rs.emulate.legacy.archive

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests the [ArchiveUtils.hash] function.
 */
class ArchiveHashTest {

    @Test
    fun test() {
        val hash = ArchiveUtils.hash(TEST_NAME).toLong()
        assertEquals(EXPECTED, hash)
    }

    companion object {

        /**
         * The expected hash of the [TEST_NAME] string.
         */
        private const val EXPECTED = 11943852L

        /**
         * The string to hash.
         */
        private const val TEST_NAME = "test"
    }

}
