package rs.emulate.modern.codec

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import rs.emulate.modern.codec.VersionedContainer.Companion.readVersionedContainer

class VersionedContainerTest {

    @Test
    fun read() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val trailer = Unpooled.buffer()
        trailer.writeShort(1337)

        val buf = Unpooled.wrappedBuffer(data, trailer)

        val container = buf.readVersionedContainer()
        assertEquals(data, container.getBuffer())
        assertEquals(1337, container.version)
    }

    @Test
    fun `read empty`() {
        assertThrows(IllegalArgumentException::class.java) {
            Unpooled.EMPTY_BUFFER.readVersionedContainer()
        }
    }

    @Test
    fun `read single byte`() {
        val buf = Unpooled.buffer()
        buf.writeByte(0x00)

        assertThrows(IllegalArgumentException::class.java) {
            buf.readVersionedContainer()
        }
    }

    @Test
    fun write() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val trailer = Unpooled.buffer()
        trailer.writeShort(1337)

        val buf = Unpooled.wrappedBuffer(data, trailer)

        val container = VersionedContainer(data, 1337)
        assertEquals(buf, container.write())
    }

    @Test
    fun checksum() {
        val data = Unpooled.wrappedBuffer("The quick brown fox jumps over the lazy dog".toByteArray())
        val container = VersionedContainer(data, 0)

        assertEquals(0x414FA339, container.checksum)
    }
}
