package rs.emulate.legacy.archive

import org.junit.Assert.assertEquals
import org.junit.Test
import rs.emulate.shared.util.DataBuffer

/**
 * Tests that the [ArchiveCodec.encode] and [ArchiveCodec.decode] methods are functioning correctly.
 */
class ArchiveTest {

    @Test
    fun test() {
        val archive = Archive(listOf(ENTRY))

        var encoded = ArchiveCodec.encode(archive, CompressionType.ARCHIVE_COMPRESSION)
        var decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive, decoded)

        encoded = ArchiveCodec.encode(archive, CompressionType.ENTRY_COMPRESSION)
        decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive, decoded)
    }

    companion object {

        /**
         * The buffer used for testing, containing 15 bytes.
         */
        private val TEST_BUFFER = DataBuffer.wrap(
            byteArrayOf(32, 78, 107, -30, 29, -81, -49, 113, 117, 51, 26, -30, -96, -34, 68)
        ).asReadOnlyBuffer()

        /**
         * The archive entry identifier used in the test.
         */
        private val TEST_IDENTIFIER = ArchiveUtils.hash("test")

        /**
         * The ArchiveEntry to test.
         */
        private val ENTRY = ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER)
    }

}
