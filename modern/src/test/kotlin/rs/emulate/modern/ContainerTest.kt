package rs.emulate.modern

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import rs.emulate.modern.Container.Companion.readContainer
import rs.emulate.modern.compression.CompressionType
import rs.emulate.modern.compression.bunzip2
import rs.emulate.modern.compression.bzip2
import rs.emulate.modern.compression.gunzip
import rs.emulate.modern.compression.gzip
import rs.emulate.util.crypto.xtea.XteaKey
import rs.emulate.util.crypto.xtea.xteaDecrypt
import rs.emulate.util.crypto.xtea.xteaEncrypt

class ContainerTest {

    @Test
    fun read() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val buf = Unpooled.wrappedBuffer(
            Unpooled.wrappedBuffer(byteArrayOf(0, 0, 0, 0, 13)),
            data.slice()
        )

        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.NONE, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun `read encrypted`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val encryptedData = data.copy()
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(
            Unpooled.wrappedBuffer(byteArrayOf(0, 0, 0, 0, 13)),
            encryptedData
        )

        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.NONE, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun `read encrypted gzip`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().gzip()

        val header = Unpooled.buffer()
        header.writeByte(2)
        header.writeInt(compressedData.readableBytes())

        val encryptedHeader = Unpooled.buffer()
        encryptedHeader.writeInt(data.readableBytes())

        val encryptedData = Unpooled.wrappedBuffer(
            encryptedHeader,
            compressedData.copy() // TODO should gzip() really return a read-only copy?
        )
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(
            header,
            encryptedData
        )

        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.GZIP, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun `read encrypted bzip2`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().bzip2()

        val header = Unpooled.buffer()
        header.writeByte(1)
        header.writeInt(compressedData.readableBytes())

        val encryptedHeader = Unpooled.buffer()
        encryptedHeader.writeInt(data.readableBytes())

        val encryptedData = Unpooled.wrappedBuffer(
            encryptedHeader,
            compressedData.copy() // TODO should bzip2() really return a read-only copy?
        )
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(
            header,
            encryptedData
        )

        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.BZIP2, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun `read gzip`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().gzip()

        val header = Unpooled.buffer()
        header.writeByte(2)
        header.writeInt(compressedData.readableBytes())
        header.writeInt(data.readableBytes())

        val buf = Unpooled.wrappedBuffer(
            header,
            compressedData
        )

        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.GZIP, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun `read bzip 2`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().bzip2()

        val header = Unpooled.buffer()
        header.writeByte(1)
        header.writeInt(compressedData.readableBytes())
        header.writeInt(data.readableBytes())

        val buf = Unpooled.wrappedBuffer(
            header,
            compressedData
        )

        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.BZIP2, container.compression)
        assertEquals(data, container.getBuffer())
    }

    @Test
    fun write() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.NONE, buf.slice())

        val out = container.write()

        assertEquals(0, out.readUnsignedByte().toInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.slice())
    }

    @Test
    fun `write encrypted`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.GZIP, buf.slice())

        val out = container.write(key).copy()

        assertEquals(2, out.readUnsignedByte().toLong())
        assertEquals((out.readableBytes() - 8).toLong(), out.readInt().toLong())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)

        assertEquals(buf.readableBytes(), decryptedData.readInt())
        assertEquals(buf, decryptedData.gunzip(buf.readableBytes()))
    }

    @Test
    fun `write encrypted gzip`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.GZIP, buf.slice())

        val out = container.write(key).copy()

        assertEquals(2, out.readUnsignedByte())
        assertEquals(out.readableBytes() - 8, out.readInt())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)

        assertEquals(buf.readableBytes(), decryptedData.readInt())
        assertEquals(buf, decryptedData.gunzip(buf.readableBytes()))
    }

    @Test
    fun `write encrypted bzip2`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.BZIP2, buf.slice())

        val out = container.write(key).copy()

        assertEquals(1, out.readUnsignedByte())
        assertEquals(out.readableBytes() - 8, out.readInt())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)

        assertEquals(buf.readableBytes(), decryptedData.readInt())
        assertEquals(buf, decryptedData.bunzip2(buf.readableBytes()))
    }

    @Test
    fun `write gzip`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.GZIP, buf.slice())

        val out = container.write()

        assertEquals(2, out.readUnsignedByte())
        assertEquals(out.readableBytes() - 8, out.readInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.gunzip(buf.readableBytes()))
    }

    @Test
    fun `write bzip2`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.BZIP2, buf.slice())

        val out = container.write()

        assertEquals(1, out.readUnsignedByte())
        assertEquals(out.readableBytes() - 8, out.readInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.bunzip2(buf.readableBytes()))
    }
}
