package rs.emulate.modern.codec

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import rs.emulate.modern.codec.Archive.Companion.readArchive

class ArchiveTest {

    @Test
    fun `read no entry`() {
        val table = ReferenceTable()
        val entry = table.createEntry(0)
        assertThrows(IllegalArgumentException::class.java) {
            Unpooled.EMPTY_BUFFER.readArchive(entry)
        }
    }

    @Test
    fun `write no entry`() {
        val archive = Archive()
        assertThrows(IllegalStateException::class.java) {
            archive.write()
        }
    }

    @Test
    fun `read single entry`() {
        val table = ReferenceTable()
        val entry = table.createEntry(0)
        entry.createChild(0)

        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val archive = buf.readArchive(entry)
        assertEquals(buf, archive[0])
    }

    @Test
    fun `write single entry`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val archive = Archive()
        archive[0] = buf

        assertEquals(archive.write(), buf)
    }

    @Test
    fun `read zero stripes`() {
        val table = ReferenceTable()
        val entry = table.createEntry(0)

        val buf = Unpooled.wrappedBuffer(byteArrayOf(0))

        assertThrows(IllegalArgumentException::class.java) {
            buf.readArchive(entry)
        }
    }

    @Test
    fun `read single stripe`() {
        val table = ReferenceTable()
        val entry = table.createEntry(0)
        entry.createChild(0)
        entry.createChild(1)
        entry.createChild(2)

        val buf0 = Unpooled.wrappedBuffer("Hello".toByteArray())
        val buf1 = Unpooled.wrappedBuffer("world".toByteArray())
        val buf2 = Unpooled.wrappedBuffer("!".toByteArray())

        val trailer = Unpooled.buffer()
        trailer.writeInt(5)
        trailer.writeInt(0)
        trailer.writeInt(-4)
        trailer.writeByte(1)

        val buf = Unpooled.wrappedBuffer(buf0, buf1, buf2, trailer)

        val archive = buf.readArchive(entry)
        assertEquals(3, archive.size)

        assertEquals(buf0, archive[0])
        assertEquals(buf1, archive[1])
        assertEquals(buf2, archive[2])
    }

    @Test
    fun `read multiple stripes`() {
        val table = ReferenceTable()
        val entry = table.createEntry(0)
        entry.createChild(0)
        entry.createChild(1)
        entry.createChild(2)

        val buf00 = Unpooled.wrappedBuffer("He".toByteArray())
        val buf01 = Unpooled.wrappedBuffer("ll".toByteArray())
        val buf02 = Unpooled.wrappedBuffer("o".toByteArray())

        val buf10 = Unpooled.wrappedBuffer("wo".toByteArray())
        val buf11 = Unpooled.wrappedBuffer("r".toByteArray())
        val buf12 = Unpooled.wrappedBuffer("ld".toByteArray())

        val buf20 = Unpooled.wrappedBuffer("".toByteArray())
        val buf21 = Unpooled.wrappedBuffer("!".toByteArray())
        val buf22 = Unpooled.wrappedBuffer("".toByteArray())

        val buf0 = Unpooled.wrappedBuffer(buf00, buf01, buf02)
        val buf1 = Unpooled.wrappedBuffer(buf10, buf11, buf12)
        val buf2 = Unpooled.wrappedBuffer(buf20, buf21, buf22)

        val trailer = Unpooled.buffer()

        trailer.writeInt(2)
        trailer.writeInt(0)
        trailer.writeInt(-2)

        trailer.writeInt(2)
        trailer.writeInt(-1)
        trailer.writeInt(0)

        trailer.writeInt(1)
        trailer.writeInt(1)
        trailer.writeInt(-2)

        trailer.writeByte(3)

        val buf = Unpooled.wrappedBuffer(buf00, buf10, buf20, buf01, buf11, buf21, buf02, buf12, buf22, trailer)

        val archive = buf.readArchive(entry)
        assertEquals(3, archive.size)

        assertEquals(buf0, archive[0])
        assertEquals(buf1, archive[1])
        assertEquals(buf2, archive[2])
    }

    @Test
    fun `write single stripe`() {
        val buf0 = Unpooled.wrappedBuffer("Hello".toByteArray())
        val buf1 = Unpooled.wrappedBuffer("world".toByteArray())
        val buf2 = Unpooled.wrappedBuffer("!".toByteArray())

        val archive = Archive()
        archive[0] = buf0.slice()
        archive[1] = buf1.slice()
        archive[2] = buf2.slice()

        val trailer = Unpooled.buffer()
        trailer.writeInt(5)
        trailer.writeInt(0)
        trailer.writeInt(-4)
        trailer.writeByte(1)

        val expected = Unpooled.wrappedBuffer(buf0, buf1, buf2, trailer)
        assertEquals(expected, archive.write())
    }
}
