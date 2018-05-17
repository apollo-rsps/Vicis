package rs.emulate.shared.util

import rs.emulate.shared.util.crypto.Whirlpool
import java.nio.ByteBuffer
import java.util.zip.CRC32


/**
 * A wrapper for [ByteBuffer] that adds methods to read unsigned data types, and specific string types. All
 * methods read and places values in big-endian format unless otherwise stated.
 */
class DataBuffer(val byteBuffer: ByteBuffer) {

    /**
     * Gets a `boolean` from this Buffer, by reading a single `byte`.
     */
    fun getBoolean(): Boolean = getByte() != 0

    /**
     * Gets a byte from this DataBuffer.
     */
    fun getByte(): Int = byteBuffer.get().toInt()

    /**
     * Reads a C-style String from this DataBuffer.
     */
    fun getCString(): String {
        val builder = StringBuilder()
        var character: Int = getUnsignedByte()

        while (character != 0) {
            builder.append(character.toChar())
            character = getUnsignedByte()
        }

        return builder.toString()
    }

    /**
     * Calculates the CRC32 checksum of this DataBuffer.
     */
    fun getCrcChecksum(): Int {
        val crc = CRC32()
        for (i in 0 until byteBuffer.limit()) {
            crc.update(byteBuffer.get(i).toInt())
        }

        return crc.value.toInt()
    }

    /**
     * Gets an `int` from this DataBuffer.
     */
    fun getInt(): Int = byteBuffer.int

    /**
     * Reads a Jag string from this DataBuffer.
     */
    fun getJagString(): String {
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

    /**
     * Gets a 'large smart' (either a short or an int).
     */
    fun getLargeSmart(): Int {
        val value = byteBuffer.get(byteBuffer.position())
        return if (value >= 0) {
            byteBuffer.short.toInt() and 0xFFFF
        } else {
            byteBuffer.int and 0x7FFFFFFF
        }
    }

    /**
     * Gets a `long` from this DataBuffer.
     */
    fun getLong(): Long = byteBuffer.long

    /**
     * Reads the remaining data in this DataBuffer into a byte array, and returns the array.
     */
    fun getRemainingBytes(): ByteArray {
        val bytes = ByteArray(byteBuffer.remaining())
        byteBuffer.get(bytes)
        return bytes
    }

    /**
     * Gets a `short` from this DataBuffer.
     */
    fun getShort(): Int = byteBuffer.short.toInt()

    /**
     * Reads a 'smart' from this DataBuffer.
     */
    fun getSmart(): Int {
        val peek = byteBuffer.get(byteBuffer.position()).toInt() and 0xFF
        return if (peek < 128) {
            getUnsignedByte()
        } else {
            getUnsignedShort() - 32768
        }
    }

    /**
     * Reads a signed 'smart' from this DataBuffer.
     */
    fun getSignedSmart(): Int {
        val peek = byteBuffer.get(byteBuffer.position()).toInt() and 0xFF
        return if (peek < 128) {
            getUnsignedByte() - 64
        } else {
            getUnsignedShort() - 49152
        }
    }

    /**
     * Gets a String, terminated with the byte value `10`, from this DataBuffer.
     */
    fun getString(): String {
        val builder = StringBuilder()
        var character = byteBuffer.get()
        while (character.toInt() != 10) {
            builder.append(character.toChar())
            character = byteBuffer.get()
        }

        return builder.toString()
    }

    /**
     * Gets an unsigned byte from this DataBuffer.
     */
    fun getUnsignedByte(): Int = byteBuffer.get().toInt() and 0xFF

    /**
     * Gets an unsigned int from this DataBuffer.
     */
    fun getUnsignedInt(): Long = byteBuffer.int.toLong() and 0xFFFFFFFF

    /**
     * Gets an unsigned `short` from this DataBuffer.
     */
    fun getUnsignedShort(): Int = byteBuffer.short.toInt() and 0xFFFF

    /**
     * Gets a tri-byte from this DataBuffer.
     */
    fun getUnsignedTriByte(): Int = getUnsignedByte() shl 16 or (getUnsignedByte() shl 8) or getUnsignedByte()

    /**
     * Returns whether or not the capacity of this Buffer is 0.
     */
    val isEmpty: Boolean
        get() = capacity() == 0

    /**
     * Returns whether or not this DataBuffer is read only.
     */
    val isReadOnly: Boolean
        get() = byteBuffer.isReadOnly

    /**
     * Creates The DataBuffer.
     */
    private constructor(bytes: ByteArray) : this(ByteBuffer.wrap(bytes))

    /**
     * Creates The DataBuffer.
     */
    private constructor(bytes: Int) : this(ByteBuffer.allocate(bytes))

    /**
     * Gets the array of bytes backing this DataBuffer.
     */
    fun array(): ByteArray {
        return byteBuffer.array()
    }

    /**
     * Returns a shallow copy of this DataBuffer as a read-only buffer.
     */
    fun asReadOnlyBuffer(): DataBuffer {
        return DataBuffer(byteBuffer.asReadOnlyBuffer())
    }

    /**
     * Gets the capacity of this DataBuffer.
     */
    fun capacity(): Int {
        return byteBuffer.capacity()
    }

    /**
     * Clears this DataBuffer.
     */
    fun clear(): DataBuffer {
        byteBuffer.clear()
        return this
    }

    /**
     * Copies the data in this DataBuffer (see [ByteBufferUtils.copy]).
     */
    fun copy(): DataBuffer {
        return DataBuffer(ByteBufferUtils.copy(byteBuffer))
    }

    /**
     * Duplicates this DataBuffer (see [ByteBuffer.duplicate]).
     */
    fun duplicate(): DataBuffer {
        return DataBuffer(byteBuffer.duplicate())
    }

    override fun equals(other: Any?): Boolean {
        if (other is DataBuffer) {
            return byteBuffer == other.byteBuffer
        }

        return false
    }

    override fun hashCode(): Int {
        return 31 * byteBuffer.hashCode()
    }

    /**
     * Fills this DataBuffer with data from the specified DataBuffer. This method fills this DataBuffer
     * until it is full (i.e. `buffer.remaining = 0`) and so the source DataBuffer must have more bytes remaining
     * than this Buffer. This method flips this DataBuffer after filling.
     */
    fun fill(source: DataBuffer): DataBuffer { // TODO rename
        val remaining = remaining()
        val sourcePosition = source.position()

        require(remaining <= source.remaining()) {
            "Source buffer must not have less remaining bytes than this DataBuffer."
        }

        byteBuffer.put(source.array(), sourcePosition, remaining)
        source.position(sourcePosition + remaining)

        return flip()
    }

    /**
     * Flips this DataBuffer.
     */
    fun flip(): DataBuffer {
        byteBuffer.flip()
        return this
    }

    /**
     * Gets `bytes.length` bytes and places them in the specified byte array.
     */
    fun read(bytes: ByteArray): DataBuffer {
        byteBuffer.get(bytes)
        return this
    }

    /**
     * Gets `length` bytes and places them in the specified byte array, starting from `offset`.
     */
    fun read(bytes: ByteArray, offset: Int, length: Int): DataBuffer {
        byteBuffer.get(bytes, offset, length)
        return this
    }

    /**
     * Places bytes from **this** buffer into the specified buffer, writing until the specified buffer is
     * filled (i.e. `buffer.remaining() == 0`).
     */
    fun read(buffer: DataBuffer): DataBuffer {
        this.byteBuffer.get(buffer.array(), buffer.position(), buffer.remaining())
        return this
    }

    /**
     * Gets the value at the specified index.
     */
    fun read(index: Int): Byte {
        return byteBuffer.get(index)
    }

    /**
     * Gets an `int` from the specified index.
     */
    fun getInt(index: Int): Int {
        return byteBuffer.getInt(index)
    }

    /**
     * Returns whether or not this DataBuffer has any bytes remaining.
     */
    fun hasRemaining(): Boolean {
        return byteBuffer.hasRemaining()
    }

    /**
     * Gets the limit of this DataBuffer.
     */
    fun limit(): Int {
        return byteBuffer.limit()
    }

    /**
     * Sets the limit of this DataBuffer.
     */
    fun limit(limit: Int): DataBuffer {
        byteBuffer.limit(limit)
        return this
    }

    /**
     * Marks this DataBuffer.
     */
    fun mark(): DataBuffer {
        byteBuffer.mark()
        return this
    }

    /**
     * Gets the position of this DataBuffer.
     */
    fun position(): Int {
        return byteBuffer.position()
    }

    /**
     * Sets the current position of this DataBuffer.
     */
    fun position(position: Int): DataBuffer {
        byteBuffer.position(position)
        return this
    }

    /**
     * Places the contents of the specified byte array into this DataBuffer.
     */
    fun put(bytes: ByteArray): DataBuffer {
        byteBuffer.put(bytes)
        return this
    }

    /**
     * Places the contents of the specified byte array into this DataBuffer, starting from `offset` and reading
     * `length` bytes.
     */
    fun put(bytes: ByteArray, offset: Int, length: Int): DataBuffer {
        byteBuffer.put(bytes, offset, length)
        return this
    }

    /**
     * Puts a [ByteBuffer] into this DataBuffer (see [ByteBuffer.put]).
     */
    fun put(buffer: ByteBuffer): DataBuffer {
        buffer.put(buffer)
        return this
    }

    /**
     * Places the contents of the specified buffer into this DataBuffer.
     */
    fun put(buffer: DataBuffer): DataBuffer {
        this.byteBuffer.put(buffer.byteBuffer)
        return this
    }

    /**
     * Puts an ASCII string into this DataBuffer, terminated with the byte value 10. Used exclusively in old clients.
     */
    fun putAsciiString(string: String): DataBuffer {
        for (c in string.toCharArray()) {
            byteBuffer.put(c.toByte())
        }

        byteBuffer.put(10.toByte())
        return this
    }

    /**
     * Places a `boolean` into this Buffer, where a `true` value places a `byte` of 1, and a
     * `false` value places a `byte` of 0.
     */
    fun putBoolean(value: Boolean): DataBuffer {
        byteBuffer.put((if (value) 1 else 0).toByte())
        return this
    }

    /**
     * Places an `int` into this Buffer, as a `boolean`, where a non-zero value places a `byte` of 1,
     * and a 0 value places a `byte` of 0.
     */
    fun putBoolean(value: Int): DataBuffer {
        return putBoolean(value != 0)
    }

    /**
     * Puts a byte into the buffer.
     */
    fun putByte(value: Int): DataBuffer {
        byteBuffer.put(value.toByte())
        return this
    }

    /**
     * Puts a 'c string' into this DataBuffer.
     */
    fun putCString(string: String): DataBuffer {
        for (c in string.toCharArray()) {
            byteBuffer.put(c.toByte())
        }

        byteBuffer.put(0.toByte())
        return this
    }

    /**
     * Puts an int into this DataBuffer.
     */
    fun putInt(value: Int): DataBuffer {
        byteBuffer.putInt(value)
        return this
    }

    /**
     * Places an int with the specified value at the specified index.
     */
    fun putInt(index: Int, value: Int): DataBuffer {
        byteBuffer.putInt(index, value)
        return this
    }

    /**
     * Puts a long into this DataBuffer.
     */
    fun putLong(value: Long): DataBuffer {
        return putInt(value.toInt() shr 32).putInt(value.toInt())
    }

    /**
     * Puts a short into this DataBuffer.
     */
    fun putShort(value: Int): DataBuffer {
        byteBuffer.putShort(value.toShort())
        return this
    }

    /**
     * Puts a tri-byte into this DataBuffer.
     */
    fun putTriByte(value: Int): DataBuffer {
        byteBuffer.put((value shr 16).toByte())
        byteBuffer.put((value shr 8).toByte())
        byteBuffer.put(value.toByte())
        return this
    }

    /**
     * Gets the amount of bytes remaining in this DataBuffer (i.e. [.limit] - [.position]).
     */
    fun remaining(): Int {
        return byteBuffer.remaining()
    }

    /**
     * Resets the mark of this DataBuffer.
     */
    fun reset(): DataBuffer {
        byteBuffer.reset()
        return this
    }

    /**
     * Skips the specified amount of bytes.
     */
    fun skip(bytes: Int): DataBuffer {
        byteBuffer.position(byteBuffer.position() + bytes)
        return this
    }

    /**
     * Slices this DataBuffer.
     */
    fun slice(): DataBuffer {
        return DataBuffer(byteBuffer.slice())
    }

    /**
     * [Whirlpool]s the data in this DataBuffer.
     */
    fun whirlpool(): DataBuffer {
        return DataBuffer(Whirlpool.whirlpool(byteBuffer))
    }

    companion object {

        fun allocate(bytes: Int): DataBuffer {
            require(bytes.toLong() >= 0) { "Buffer capacity cannot be negative." }
            return DataBuffer(bytes)
        }

        /**
         * Wraps the specified byte array in a new DataBuffer.
         */
        fun wrap(bytes: ByteArray): DataBuffer {
            return DataBuffer(bytes)
        }

        /**
         * Wraps the specified [ByteBuffer] in a new DataBuffer.
         */
        fun wrap(buffer: ByteBuffer): DataBuffer {
            return DataBuffer(buffer)
        }
    }

}
