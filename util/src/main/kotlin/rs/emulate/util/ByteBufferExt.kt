@file:Suppress("UsePropertyAccessSyntax")

package rs.emulate.util

import rs.emulate.util.crypto.Whirlpool
import java.nio.ByteBuffer
import java.util.zip.CRC32

fun ByteBuffer.copy(): ByteBuffer {
    val copy = ByteBuffer.allocate(remaining())
    mark()
    copy.put(this).flip()
    reset()
    return copy
}

fun ByteBuffer.get(dest: ByteBuffer) {
    while (dest.hasRemaining()) {
        dest.put(get())
    }
}

fun ByteBuffer.isEmpty(): Boolean {
    return capacity() == 0
}

fun ByteBuffer.getRemainingBytes(): ByteArray {
    return ByteArray(remaining()).also { get(it) }
}

inline fun ByteBuffer.getBoolean(predicate: (Int) -> Boolean = { it == 1 }): Boolean {
    return predicate(get().toInt())
}

fun ByteBuffer.putBoolean(value: Boolean): ByteBuffer {
    put((if (value) 1 else 0).toByte())
    return this
}

fun ByteBuffer.getByte(): Int = get().toInt()
fun ByteBuffer.putByte(value: Int): ByteBuffer = put(value.toByte())
fun ByteBuffer.getUnsignedByte(): Int = get().toInt() and 0xFF
fun ByteBuffer.getUnsignedShort(): Int = getShort().toInt() and 0xFFFF
fun ByteBuffer.getUnsignedTriByte(): Int = getUnsignedByte() shl 16 or (getUnsignedByte() shl 8) or getUnsignedByte()
fun ByteBuffer.getUnsignedInt(): Long = getInt().toLong() and 0xFFFFFFFF

fun ByteBuffer.getUnsignedIntSmart(): Int {
    val peek = get(position()).toInt()

    return if (peek and 0x80 == 0) {
        getShort().toInt() and 0x7fff
    } else {
        getInt() and 0x7fffffff
    }
}

fun ByteBuffer.putTriByte(value: Int): ByteBuffer {
    put((value shr 16).toByte())
    put((value shr 8).toByte())
    put(value.toByte())
    return this
}

fun ByteBuffer.getSmart(): Int {
    val peek = get(position()).toInt() and 0xFF

    return if (peek < 128) {
        getUnsignedByte()
    } else {
        getUnsignedShort() - 32768
    }
}

fun ByteBuffer.getSignedSmart(): Int {
    val peek = get(position()).toInt() and 0xFF
    return if (peek < 128) {
        getUnsignedByte() - 64
    } else {
        getUnsignedShort() - 49152
    }
}

fun ByteBuffer.getLargeSmart(): Int {
    val value = get(position())

    return if (value >= 0) {
        getShort().toInt() and 0xFFFF
    } else {
        getInt() and 0x7FFFFFFF
    }
}

fun ByteBuffer.putMedium(v: Int) {
    put((v shr 16).toByte())
    put((v shr 8).toByte())
    put(v.toByte())
}

fun ByteBuffer.getUnsignedMedium(): Int {
    var v = 0
    v = v or (get().toInt() and 0xFF shl 16)
    v = v or (get().toInt() and 0xFF shl 8)
    v = v or (get().toInt() and 0xFF)
    return v
}


fun ByteBuffer.getAsciiString(): String {
    val builder = StringBuilder()
    var character = get()

    while (character.toInt() != 10) {
        builder.append(character.toChar())
        character = get()
    }

    return builder.toString()
}

fun ByteBuffer.putAsciiString(string: String): ByteBuffer {
    for (c in string.toCharArray()) {
        put(c.toByte())
    }

    put(10.toByte())
    return this
}

fun ByteBuffer.getCString(): String {
    val builder = StringBuilder()
    var character: Int = getUnsignedByte()

    while (character != 0) {
        builder.append(character.toChar())
        character = getUnsignedByte()
    }

    return builder.toString()
}

fun ByteBuffer.putCString(string: String): ByteBuffer {
    for (c in string.toCharArray()) {
        put(c.toByte())
    }

    put(0.toByte())
    return this
}

fun ByteBuffer.getJagString(): String {
    val builder = StringBuilder()
    var value: Int = getUnsignedByte()

    while (value != 0) {
        if (value in 127..159) {
            val character = CacheStringUtils.CHARACTERS[value - 128]

            if (character.toInt() != 0) {
                builder.append(character)
            }
        } else {
            builder.append(value.toChar())
        }

        value = getUnsignedByte()
    }

    return builder.toString()
}

fun ByteBuffer.getCrcChecksum(): Int {
    val crc = CRC32()
    for (i in 0 until limit()) {
        crc.update(get(i).toInt())
    }

    return crc.value.toInt()
}

fun ByteBuffer.whirlpool(): ByteBuffer {
    return Whirlpool.whirlpool(this)
}
