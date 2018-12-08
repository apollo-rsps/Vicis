package rs.emulate.legacy.archive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

/**
 * Tests that the [ArchiveCodec.encode] and [ArchiveCodec.decode] methods are functioning correctly.
 */
class ArchiveTest {

    @Test
    fun `archive encoding using archive compression`() {
        val archive = Archive(listOf(ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER)))

        val encoded = ArchiveCodec.encode(archive, CompressionType.ARCHIVE_COMPRESSION)
        val decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive.entries.first().buffer, decoded.entries.first().buffer)
    }

    @Test
    fun `archive encoding using entry compression`() {
        val archive = Archive(listOf(ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER)))

        val encoded = ArchiveCodec.encode(archive, CompressionType.ENTRY_COMPRESSION)
        val decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive, decoded)
    }

    companion object {

        private val TEST_BUFFER = ByteBuffer.wrap(
            byteArrayOf(32, 78, 107, -30, 29, -81, -49, 113, 117, 51, 26, -30, -96, -34, 68)
        ).asReadOnlyBuffer()

        private val TEST_IDENTIFIER = ArchiveUtils.hash("test")

    }

}
