package rs.emulate.legacy.archive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rs.emulate.legacy.archive.Archive.Companion.entryHash
import java.nio.ByteBuffer

class ArchiveTest {

    @Test
    fun `end-to-end archive serialization using archive compression`() {
        val archive = Archive(listOf(ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER)))

        val encoded = ArchiveCodec.encode(archive, CompressionType.ARCHIVE_COMPRESSION)
        val decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive, decoded)
    }

    @Test
    fun `end-to-end archive serialization using entry compression`() {
        val archive = Archive(listOf(ArchiveEntry(TEST_IDENTIFIER, TEST_BUFFER)))

        val encoded = ArchiveCodec.encode(archive, CompressionType.ENTRY_COMPRESSION)
        val decoded = ArchiveCodec.decode(encoded)

        assertEquals(archive, decoded)
    }

    @Test
    fun `archive entry name hashing`() {
        assertEquals(11943852, "test".entryHash())
    }

    companion object {

        private val TEST_BUFFER = ByteBuffer.wrap(
            byteArrayOf(32, 78, 107, -30, 29, -81, -49, 113, 117, 51, 26, -30, -96, -34, 68)
        ).asReadOnlyBuffer()

        private val TEST_IDENTIFIER = "test".entryHash()

    }

}
