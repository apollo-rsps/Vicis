package rs.emulate.modern

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ChecksumTableTest {

    @Test
    fun read() {
        val buf = Unpooled.buffer()

        buf.writeInt(0x12345678)
        buf.writeInt(-0x54321055)

        buf.writeInt(-0x789abcdf)
        buf.writeInt(-0x45012346)

        val table = buf.readChecksumTable()
        assertEquals(2, table.size)

        var entry = table[0]
        assertEquals(0x12345678, entry.checksum)
        assertEquals(-0x54321055, entry.version)

        entry = table[1]
        assertEquals(-0x789abcdf, entry.checksum)
        assertEquals(-0x45012346, entry.version)
    }

    @Test
    fun `read when not multiple of 8`() {
        val buf = Unpooled.wrappedBuffer(byteArrayOf(0))
        assertThrows(IllegalArgumentException::class.java) {
            buf.readChecksumTable()
        }
    }

    @Test
    fun write() {
        val table = ChecksumTable()

        var entry = table.addEntry()
        entry.checksum = 0x12345678
        entry.version = -0x54321055

        entry = table.addEntry()
        entry.checksum = -0x789abcdf
        entry.version = -0x45012346

        val buf = table.write()
        assertEquals(0x12345678, buf.readInt())
        assertEquals(-0x54321055, buf.readInt())

        assertEquals(-0x789abcdf, buf.readInt())
        assertEquals(-0x45012346, buf.readInt())

        assertFalse(buf.isReadable)
    }
}
