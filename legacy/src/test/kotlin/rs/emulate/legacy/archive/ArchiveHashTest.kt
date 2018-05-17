package rs.emulate.legacy.archive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


/**
 * Tests the [ArchiveUtils.hash] function.
 */
class ArchiveHashTest {

    @Test
    fun test() {
        val hash = ArchiveUtils.hash(TEST_NAME)
        assertEquals(EXPECTED, hash)
    }

    companion object {

        /**
         * The expected hash of the [TEST_NAME] string.
         */
        private const val EXPECTED = 11943852

        /**
         * The string to hash.
         */
        private const val TEST_NAME = "test"
    }

}
