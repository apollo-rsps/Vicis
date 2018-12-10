package rs.emulate.util

import io.netty.buffer.ByteBuf
import io.netty.util.ByteProcessor
import rs.emulate.util.charset.Cp1252Charset
import java.nio.charset.Charset
import java.util.zip.CRC32

fun ByteBuf.toByteArray(): ByteArray {
    if (hasArray()) {
        return array()
    }

    return ByteArray(readableBytes()).also { readBytes(it) }
}

fun ByteBuf.crc32(): Int {
    val bytes: ByteArray
    val off: Int
    val len: Int

    if (hasArray()) {
        off = arrayOffset() + readerIndex()
        len = readableBytes()
        bytes = array()
    } else {
        off = 0
        len = readableBytes()

        bytes = ByteArray(len)
        getBytes(readerIndex(), bytes)
    }

    val crc32 = CRC32()
    crc32.update(bytes, off, len)
    return crc32.value.toInt()
}

fun ByteBuf.readString(charset: Charset = Cp1252Charset): String {
    val start = readerIndex()

    val end = forEachByte(ByteProcessor.FIND_NUL)
    require(end != -1) { "Unterminated string" }

    val len = end - start

    val bytes = ByteArray(len)
    readBytes(bytes)
    readerIndex(readerIndex() + 1)
    return String(bytes, charset)
}

fun ByteBuf.readAsciiString(): String {
    val builder = StringBuilder()
    var character = readByte()

    while (character.toInt() != 10) {
        builder.append(character.toChar())
        character = readByte()
    }

    return builder.toString()
}

fun ByteBuf.writeAsciiString(string: String) {
    for (c in string.toCharArray()) {
        writeByte(c.toInt())
    }

    writeByte(10)
}

fun ByteBuf.writeCString(str: String, charset: Charset = Cp1252Charset) {
    val bytes = str.toByteArray(charset)
    writeBytes(bytes)
    writeByte(0)
}

fun ByteBuf.readVersionedString(charset: Charset = Cp1252Charset): String {
    val version = readUnsignedByte().toInt()
    require(version == 0)
    return readString(charset)
}

fun ByteBuf.writeVersionedString(str: String) {
    writeByte(0)
    writeCString(str)
}

fun ByteBuf.readOptionalString(charset: Charset = Cp1252Charset): String? {
    val present = readBoolean()
    return if (present) readString(charset) else null
}

fun ByteBuf.writeOptionalString(str: String?, charset: Charset = Cp1252Charset) {
    if (str != null) {
        writeBoolean(true)
        writeCString(str, charset)
    } else {
        writeBoolean(false)
    }
}

fun ByteBuf.readUnsignedSmart(): Int {
    val peek = getByte(readerIndex()).toInt()
    return if (peek and 0x80 == 0) {
        readByte().toInt() and 0x7f
    } else {
        readShort().toInt() and 0x7fff
    }
}

fun ByteBuf.writeUnsignedSmart(value: Int) {
    when (value) {
        in 0..0x7f -> writeByte(value)
        in 0..0x7fff -> writeShort(0x8000 or value)
        else -> throw IllegalArgumentException("Expected $value to fall within range [${0..0x7fff}]")
    }
}

fun ByteBuf.readSignedSmart(): Int {
    val peek = getByte(readerIndex()).toInt() and 0xFF
    return if (peek < 128) {
        readUnsignedByte() - 64
    } else {
        readUnsignedShort() - 49152
    }
}

fun ByteBuf.readUnsignedTriByte(): Int {
    return readUnsignedByte().toInt() shl 16 or (readUnsignedByte().toInt() shl 8) or readUnsignedByte().toInt()
}

fun ByteBuf.writeTriByte(value: Int) {
    writeByte(value shr 16 and 0xFF)
    writeByte(value shr 8 and 0xFF)
    writeByte(value and 0xFF)
}

fun ByteBuf.readUnsignedMultiSmart(): Int {
    var total = 0
    var value: Int

    do {
        value = readUnsignedSmart()
        total += value
    } while (value == 0x7fff)

    return total
}

fun ByteBuf.writeUnsignedMultiSmart(value: Int) {
    require(value >= 0)

    var remaining = value
    while (remaining >= 0x7fff) {
        writeUnsignedSmart(0x7fff)
        remaining -= 0x7fff
    }

    writeUnsignedSmart(remaining)
}

fun ByteBuf.readUnsignedIntSmart(): Int {
    val peek = getByte(readerIndex()).toInt()
    return if (peek and 0x80 == 0) {
        readShort().toInt() and 0x7fff
    } else {
        readInt() and 0x7fffffff
    }
}

fun ByteBuf.writeUnsignedIntSmart(value: Int) {
    when (value) {
        in 0..0x7fff -> writeShort(value)
        in 0..0x7fffffff -> writeInt(-0x80000000 or value)
        else -> throw IllegalArgumentException("Expected $value to fall within range [${0..0x7fffffff}]")
    }
}

fun ByteBuf.readVarInt(): Int {
    var value = 0

    var v: Int
    do {
        v = readUnsignedByte().toInt()

        value = value shl 7
        value = value or (v and 0x7f)
    } while (v and 0x80 != 0)

    return value
}

fun ByteBuf.writeVarInt(value: Int) {
    if (value and 0x7f.inv() != 0) {
        if (value and 0x3fff.inv() != 0) {
            if (value and 0x1fffff.inv() != 0) {
                if (value and 0xfffffff.inv() != 0) {
                    writeByte(((value ushr 28) and 0x7f) or 0x80)
                }

                writeByte(((value ushr 21) and 0x7f) or 0x80)
            }

            writeByte(((value ushr 14) and 0x7f) or 0x80)
        }

        writeByte(((value ushr 7) and 0x7f) or 0x80)
    }

    writeByte(value and 0x7f)
}

fun ByteBuf.readVarLong(bytes: Int): Long {
    require(bytes in 1..7)

    var value = 0L
    var bits = (bytes - 1) * 8
    while (bits >= 0) {
        value = value or (readUnsignedByte().toInt() shl bits).toLong()
        bits -= 8
    }
    return value
}

fun ByteBuf.writeVarLong(bytes: Int, value: Long) {
    require(bytes in 1..7)

    var bits = (bytes - 1) * 8
    while (bits >= 0) {
        writeByte((value shr bits).toInt())
        bits -= 8
    }
}
