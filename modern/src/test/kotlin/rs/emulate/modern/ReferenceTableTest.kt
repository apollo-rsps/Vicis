package rs.emulate.modern

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import rs.emulate.modern.ReferenceTable.Companion.readRefTable

class ReferenceTableTest {

    private val buf = Unpooled.buffer().apply {

        /* header */
        writeByte(6)   /* format */
        writeInt(1337) /* version */
        writeByte(0x1) /* flags */

        writeShort(3) /* size */

        /* entry id deltas */
        writeShort(3)
        writeShort(1)
        writeShort(6)

        /* entry name hashes */
        writeInt(0x1234)
        writeInt(0x5678)
        writeInt(0x9ABC)

        /* entry checksums */
        writeInt(0xDDDD)
        writeInt(0xEEEE)
        writeInt(0xFFFF)

        /* entry versions */
        writeInt(1)
        writeInt(2)
        writeInt(3)

        /* child sizes */
        writeShort(0)
        writeShort(1)
        writeShort(3)

        /* child id deltas */
        writeShort(1)
        writeShort(1)
        writeShort(3)
        writeShort(6)

        /* child name hashes */
        writeInt(0x0123)
        writeInt(0x4567)
        writeInt(0x89AB)
        writeInt(0xCDEF)
    }

    @Test
    fun read() {
        val table = buf.readRefTable()

        assertEquals(6, table.format)
        assertEquals(1337, table.version)
        assertTrue(table.nameHashingEnabled)

        assertEquals(3, table.size)
        assertEquals(11, table.capacity)

        for (i in 0..10) {
            if (i == 3 || i == 4 || i == 10) {
                assertTrue(table.containsEntry(i))
            } else {
                assertFalse(table.containsEntry(i))
            }
        }

        var entry: ReferenceTable.Entry = table.getEntry(3)!!
        assertEquals(0x1234, entry.nameHash)
        assertEquals(0xDDDD, entry.checksum)
        assertEquals(1, entry.version)
        assertEquals(0, entry.size)
        assertEquals(0, entry.capacity)

        entry = table.getEntry(4)!!
        assertEquals(0x5678, entry.nameHash)
        assertEquals(0xEEEE, entry.checksum)
        assertEquals(2, entry.version)
        assertEquals(1, entry.size)
        assertEquals(2, entry.capacity)

        assertTrue(1 in entry)

        var child: ReferenceTable.ChildEntry = entry[1]!!
        assertEquals(0x0123, child.nameHash)

        entry = table.getEntry(10)!!
        assertEquals(0x9ABC, entry.nameHash)
        assertEquals(0xFFFF, entry.checksum)
        assertEquals(3, entry.version)
        assertEquals(3, entry.size)
        assertEquals(11, entry.capacity)

        child = entry[1]!!
        assertEquals(0x4567, child.nameHash)

        child = entry[4]!!
        assertEquals(0x89AB, child.nameHash)

        child = entry[10]!!
        assertEquals(0xCDEF, child.nameHash)
    }

    @Test
    fun write() {
        val table = ReferenceTable()
        table.version = 1337
        table.nameHashingEnabled = true

        var entry: ReferenceTable.Entry = table.createEntry(3)
        entry.nameHash = 0x1234
        entry.checksum = 0xDDDD
        entry.version = 1

        entry = table.createEntry(4)
        entry.nameHash = 0x5678
        entry.checksum = 0xEEEE
        entry.version = 2

        var child: ReferenceTable.ChildEntry = entry.createChild(1)
        child.nameHash = 0x0123

        entry = table.createEntry(10)
        entry.nameHash = 0x9ABC
        entry.checksum = 0xFFFF
        entry.version = 3

        child = entry.createChild(1)
        child.nameHash = 0x4567

        child = entry.createChild(4)
        child.nameHash = 0x89AB

        child = entry.createChild(10)
        child.nameHash = 0xCDEF

        assertEquals(buf, table.write())
    }

    @Test
    fun `lookup by name hash`() {
        val table = ReferenceTable()

        val entry = table.createEntry(123)
        entry.setName("hello")

        assertEquals(entry, table.getEntry("hello"))
        assertNull(table.getEntry("non-existent"))
    }
}
