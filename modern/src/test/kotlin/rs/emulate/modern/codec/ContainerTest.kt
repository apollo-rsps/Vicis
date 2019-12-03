package rs.emulate.modern.codec

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import rs.emulate.modern.codec.Container.Companion.readContainer
import rs.emulate.util.compression.*
import rs.emulate.util.crypto.xtea.XteaKey
import rs.emulate.util.crypto.xtea.xteaDecrypt
import rs.emulate.util.crypto.xtea.xteaEncrypt

class ContainerTest {

    @Test
    fun `read uncompressed`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())

        val buf = Unpooled.wrappedBuffer(
            Unpooled.wrappedBuffer(byteArrayOf(CompressionType.NONE.id.toByte(), 0, 0, 0, data.writerIndex().toByte())),
            data.slice()
        )

        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.NONE, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read gzip`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().gzip()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.GZIP.id)
        header.writeInt(compressedData.readableBytes())
        header.writeInt(data.readableBytes())

        val buf = Unpooled.wrappedBuffer(header, compressedData)
        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.GZIP, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read bzip2`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().bzip2()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.BZIP2.id)
        header.writeInt(compressedData.readableBytes())
        header.writeInt(data.readableBytes())

        val buf = Unpooled.wrappedBuffer(header, compressedData)
        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.BZIP2, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read lzma`() {
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().lzma()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.LZMA.id)
        header.writeInt(compressedData.readableBytes())
        header.writeInt(data.readableBytes())

        val buf = Unpooled.wrappedBuffer(header, compressedData)
        val container = buf.readContainer()

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.LZMA, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read encrypted`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val encryptedData = data.copy()
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(
            Unpooled.wrappedBuffer(byteArrayOf(CompressionType.NONE.id.toByte(), 0, 0, 0, data.writerIndex().toByte())),
            encryptedData
        )

        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.NONE, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read encrypted gzip`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().gzip()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.GZIP.id)
        header.writeInt(compressedData.readableBytes())

        val encryptedHeader = Unpooled.buffer()
        encryptedHeader.writeInt(data.readableBytes())

        val encryptedData = Unpooled.wrappedBuffer(
            encryptedHeader,
            compressedData.copy() // TODO should gzip() really return a read-only copy?
        )
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(header, encryptedData)
        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.GZIP, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read encrypted bzip2`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().bzip2()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.BZIP2.id)
        header.writeInt(compressedData.readableBytes())

        val encryptedHeader = Unpooled.buffer()
        encryptedHeader.writeInt(data.readableBytes())

        val encryptedData = Unpooled.wrappedBuffer(
            encryptedHeader,
            compressedData.copy() // TODO should bzip2() really return a read-only copy?
        )
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(header, encryptedData)
        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.BZIP2, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `read encrypted lzma`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val data = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val compressedData = data.slice().lzma()

        val header = Unpooled.buffer()
        header.writeByte(CompressionType.LZMA.id)
        header.writeInt(compressedData.readableBytes())

        val encryptedHeader = Unpooled.buffer()
        encryptedHeader.writeInt(data.readableBytes())

        val encryptedData = Unpooled.wrappedBuffer(
            encryptedHeader,
            compressedData.copy() // TODO should lzma() really return a read-only copy?
        )
        encryptedData.xteaEncrypt(encryptedData.readerIndex(), encryptedData.readableBytes(), key)

        val buf = Unpooled.wrappedBuffer(header, encryptedData)
        val container = buf.readContainer(key)

        assertFalse(buf.isReadable)
        assertEquals(CompressionType.LZMA, container.compression)
        assertEquals(data, container.buffer)
    }

    @Test
    fun `write uncompressed`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.NONE, buf.slice())

        val out = container.write()

        assertEquals(CompressionType.NONE.id, out.readUnsignedByte().toInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.slice())
    }

    @Test
    fun `write gzip`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.GZIP, buf.slice())

        val out = container.write()

        assertEquals(CompressionType.GZIP.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 8, out.readInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.gunzip(buf.readableBytes()))
    }

    @Test
    fun `write bzip2`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.BZIP2, buf.slice())

        val out = container.write()

        assertEquals(CompressionType.BZIP2.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 8, out.readInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.bunzip2(buf.readableBytes()))
    }

    @Test
    fun `write lzma`() {
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.LZMA, buf.slice())

        val out = container.write()

        assertEquals(CompressionType.LZMA.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 8, out.readInt())
        assertEquals(buf.readableBytes(), out.readInt())
        assertEquals(buf, out.unlzma(buf.readableBytes()))
    }

    @Test
    fun `write encrypted`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.NONE, buf.slice())

        val out = container.write(key).copy()

        assertEquals(CompressionType.NONE.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 4, out.readInt())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)
        assertEquals(buf, decryptedData)
    }

    @Test
    fun `write encrypted gzip`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.GZIP, buf.slice())

        val out = container.write(key).copy()

        assertEquals(CompressionType.GZIP.id, out.readUnsignedByte().toInt())
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

        assertEquals(CompressionType.BZIP2.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 8, out.readInt())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)

        assertEquals(buf.readableBytes(), decryptedData.readInt())
        assertEquals(buf, decryptedData.bunzip2(buf.readableBytes()))
    }

    @Test
    fun `write encrypted lzma`() {
        val key = XteaKey.fromString("0123456789abcdef0123456789abcdef")
        val buf = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val container = Container(CompressionType.LZMA, buf.slice())

        val out = container.write(key).copy()

        assertEquals(CompressionType.LZMA.id, out.readUnsignedByte().toInt())
        assertEquals(out.readableBytes() - 8, out.readInt())

        val decryptedData = out.slice()
        decryptedData.xteaDecrypt(decryptedData.readerIndex(), decryptedData.readableBytes(), key)

        assertEquals(buf.readableBytes(), decryptedData.readInt())
        assertEquals(buf, decryptedData.unlzma(buf.readableBytes()))
    }
}
