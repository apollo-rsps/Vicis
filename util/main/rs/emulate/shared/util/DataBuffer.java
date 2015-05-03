package rs.emulate.shared.util;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import rs.emulate.shared.util.crypto.Whirlpool;
import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A wrapper for {@link ByteBuffer} that adds methods to read unsigned data types, and specific string types. All
 * methods read and places values in big-endian format unless otherwise stated.
 *
 * @author Major
 */
public final class DataBuffer {

	/**
	 * Allocates a new DataBuffer.
	 *
	 * @param bytes The capacity of the buffer.
	 * @return The DataBuffer.
	 */
	public static DataBuffer allocate(int bytes) {
		Assertions.checkNonNegative(bytes, "Buffer capacity cannot be negative.");
		return new DataBuffer(bytes);
	}

	/**
	 * Wraps the specified byte array in a new DataBuffer.
	 *
	 * @param bytes The byte array to wrap.
	 * @return The DataBuffer.
	 */
	public static DataBuffer wrap(byte[] bytes) {
		return new DataBuffer(bytes);
	}

	/**
	 * Wraps the specified {@link ByteBuffer} in a new DataBuffer.
	 *
	 * @param buffer The byte buffer.
	 * @return The DataBuffer.
	 */
	public static DataBuffer wrap(ByteBuffer buffer) {
		return new DataBuffer(buffer);
	}

	/**
	 * The byte buffer backing this DataBuffer.
	 */
	private final ByteBuffer buffer;

	/**
	 * Creates The DataBuffer.
	 *
	 * @param bytes The byte array to wrap.
	 */
	private DataBuffer(byte[] bytes) {
		this(ByteBuffer.wrap(bytes));
	}

	/**
	 * Creates The DataBuffer.
	 *
	 * @param buffer The backing {@link ByteBuffer}.
	 */
	private DataBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Creates The DataBuffer.
	 *
	 * @param bytes The capacity of the buffer.
	 */
	private DataBuffer(int bytes) {
		this(ByteBuffer.allocate(bytes));
	}

	/**
	 * Gets the array of bytes backing this DataBuffer.
	 *
	 * @return The array of bytes.
	 */
	public byte[] array() {
		return buffer.array();
	}

	/**
	 * Returns a shallow copy of this DataBuffer as a read-only buffer.
	 *
	 * @return The read-only buffer.
	 */
	public DataBuffer asReadOnlyBuffer() {
		return new DataBuffer(buffer.asReadOnlyBuffer());
	}

	/**
	 * Gets the capacity of this DataBuffer.
	 *
	 * @return The capacity.
	 */
	public int capacity() {
		return buffer.capacity();
	}

	/**
	 * Clears this DataBuffer.
	 *
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer clear() {
		buffer.clear();
		return this;
	}

	/**
	 * Copies the data in this DataBuffer (see {@link ByteBufferUtils#copy}).
	 *
	 * @return The copied buffer.
	 */
	public DataBuffer copy() {
		return new DataBuffer(ByteBufferUtils.copy(buffer));
	}

	/**
	 * Duplicates this DataBuffer (see {@link ByteBuffer#duplicate}).
	 *
	 * @return The duplicate buffer.
	 */
	public DataBuffer duplicate() {
		return new DataBuffer(buffer.duplicate());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataBuffer) {
			DataBuffer other = (DataBuffer) obj;
			return buffer.equals(other.buffer);
		}

		return false;
	}

	/**
	 * Fills <strong>this</strong> DataBuffer with data from the specified DataBuffer. This method fills this DataBuffer
	 * until it is full (i.e. {@code buffer.remaining = 0}) and so the source DataBuffer must have more bytes remaining
	 * than this Buffer. This method flips this DataBuffer after filling.
	 *
	 * @param source The source DataBuffer.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer fill(DataBuffer source) { // TODO rename
		int remaining = remaining(), sourcePosition = source.position();

		Preconditions.checkArgument(remaining <= source.remaining(),
				"Source buffer must not have less remaining bytes than this DataBuffer.");

		buffer.put(source.array(), sourcePosition, remaining);
		source.position(sourcePosition + remaining);
		return this.flip();
	}

	/**
	 * Flips this DataBuffer.
	 *
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer flip() {
		buffer.flip();
		return this;
	}

	/**
	 * Gets {@code bytes.length} bytes and places them in the specified byte array.
	 *
	 * @param bytes The byte array.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer get(byte[] bytes) {
		buffer.get(bytes);
		return this;
	}

	/**
	 * Gets {@code length} bytes and places them in the specified byte array, starting from {@code offset}.
	 *
	 * @param bytes The byte array.
	 * @param offset The byte array offset.
	 * @param length The amount of bytes to place.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer get(byte[] bytes, int offset, int length) {
		buffer.get(bytes, offset, length);
		return this;
	}

	/**
	 * Places bytes from <strong>this</strong> buffer into the specified buffer, writing until the specified buffer is
	 * filled (i.e. {@code buffer.remaining() == 0}).
	 *
	 * @param buffer The byte buffer.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer get(DataBuffer buffer) {
		this.buffer.get(buffer.array(), buffer.position(), buffer.remaining());
		return this;
	}

	/**
	 * Gets the value at the specified index.
	 *
	 * @param index The index.
	 * @return The value.
	 */
	public byte get(int index) {
		return buffer.get(index);
	}

	/**
	 * Gets a {@code boolean} from this Buffer, by reading a single {@code byte}.
	 *
	 * @return {@code true} if the byte is non-zero, otherwise {@code false}.
	 */
	public boolean getBoolean() {
		return getByte() != 0;
	}

	/**
	 * Gets a byte from this DataBuffer.
	 *
	 * @return The byte.
	 */
	public int getByte() {
		return buffer.get();
	}

	/**
	 * Gets the byte buffer backing this DataBuffer.
	 *
	 * @return The byte buffer.
	 */
	public ByteBuffer getByteBuffer() {
		return buffer;
	}

	/**
	 * Calculates the CRC32 checksum of this DataBuffer.
	 *
	 * @return The CRC32 checksum.
	 */
	public int getCrcChecksum() {
		Checksum crc = new CRC32();
		for (int i = 0; i < buffer.limit(); i++) {
			crc.update(buffer.get(i));
		}

		return (int) crc.getValue();
	}

	/**
	 * Reads a C-style String from this DataBuffer.
	 *
	 * @return The C-string.
	 */
	public String getCString() {
		StringBuilder builder = new StringBuilder();
		int character;

		while ((character = getUnsignedByte()) != 0) {
			builder.append((char) character);
		}

		return builder.toString();
	}

	/**
	 * Gets an {@code int} from this DataBuffer.
	 *
	 * @return The {@code int}.
	 */
	public int getInt() {
		return buffer.getInt();
	}

	/**
	 * Gets an {@code int} from the specified index.
	 *
	 * @param index The index.
	 * @return The {@code int}.
	 */
	public int getInt(int index) {
		return buffer.getInt(index);
	}

	/**
	 * Reads a Jag string from this DataBuffer.
	 *
	 * @return The string.
	 */
	public String getJagString() {
		StringBuilder builder = new StringBuilder();
		int value;

		while ((value = getUnsignedByte()) != 0) {
			if (value >= 127 && value < 160) {
				char character = CacheStringUtils.CHARACTERS[value - 128];

				if (character != 0) {
					builder.append(character);
				}
			} else {
				builder.append((char) value);
			}
		}

		return builder.toString();
	}

	/**
	 * Gets a 'large smart' (either a short or an int).
	 *
	 * @return The value.
	 */
	public int getLargeSmart() {
		byte value = buffer.get(buffer.position());
		if (value >= 0) {
			return buffer.getShort() & 0xFFFF;
		}

		return buffer.getInt() & 0x7FFF_FFFF;
	}

	/**
	 * Gets a {@code long} from this DataBuffer.
	 *
	 * @return The {@code long}.
	 */
	public long getLong() {
		return buffer.getLong();
	}

	/**
	 * Reads the remaining data in this DataBuffer into a byte array, and returns the array.
	 *
	 * @return The byte array containing the remaining data.
	 */
	public byte[] getRemainingBytes() {
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		return bytes;
	}

	/**
	 * Gets a {@code short} from this DataBuffer.
	 *
	 * @return The {@code short}.
	 */
	public int getShort() {
		return buffer.getShort();
	}

	/**
	 * Reads a 'smart' from this DataBuffer.
	 *
	 * @return The smart.
	 */
	public int getSmart() {
		int peek = buffer.get(buffer.position()) & 0xFF;
		if (peek < 128) {
			return getUnsignedByte();
		}

		return getUnsignedShort() - 32768;
	}

	/**
	 * Gets a String, terminated with the byte value {@code 10}, from this DataBuffer.
	 *
	 * @return The String.
	 */
	public String getString() {
		StringBuilder builder = new StringBuilder();
		char character;
		while ((character = (char) buffer.get()) != 10) {
			builder.append(character);
		}

		return builder.toString();
	}

	/**
	 * Gets an unsigned byte from this DataBuffer.
	 *
	 * @return The unsigned byte.
	 */
	public int getUnsignedByte() {
		return buffer.get() & 0xFF;
	}

	/**
	 * Gets an unsigned int from this DataBuffer.
	 *
	 * @return The unsigned int, as a long.
	 */
	public long getUnsignedInt() {
		return buffer.getInt() & 0xFFFF_FFFF;
	}

	/**
	 * Gets an unsigned {@code short} from this DataBuffer.
	 *
	 * @return The unsigned {@code short}.
	 */
	public int getUnsignedShort() {
		return buffer.getShort() & 0xFFFF;
	}

	/**
	 * Gets a tri-byte from this DataBuffer.
	 *
	 * @return The tri-byte.
	 */
	public int getUnsignedTriByte() {
		return getUnsignedByte() << 16 | getUnsignedByte() << 8 | getUnsignedByte();
	}

	@Override
	public int hashCode() {
		return 31 * buffer.hashCode();
	}

	/**
	 * Returns whether or not this DataBuffer has any bytes remaining.
	 *
	 * @return {@code true} if there are any bytes remaining, {@code false} if not.
	 */
	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}

	/**
	 * Returns whether or not the capacity of this Buffer is 0.
	 *
	 * @return {@code true} if the capacity is 0, {@code false} if not.
	 */
	public boolean isEmpty() {
		return capacity() == 0;
	}

	/**
	 * Returns whether or not this DataBuffer is read only.
	 *
	 * @return {@code true} if this DataBuffer is read only, {@code false} if not.
	 */
	public boolean isReadOnly() {
		return buffer.isReadOnly();
	}

	/**
	 * Gets the limit of this DataBuffer.
	 *
	 * @return The limit.
	 */
	public int limit() {
		return buffer.limit();
	}

	/**
	 * Sets the limit of this DataBuffer.
	 *
	 * @param limit The new limit.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer limit(int limit) {
		buffer.limit(limit);
		return this;
	}

	/**
	 * Marks this DataBuffer.
	 *
	 * @return The DataBuffer..
	 */
	public DataBuffer mark() {
		buffer.mark();
		return this;
	}

	/**
	 * Gets the position of this DataBuffer.
	 *
	 * @return The position.
	 */
	public int position() {
		return buffer.position();
	}

	/**
	 * Sets the current position of this DataBuffer.
	 *
	 * @param position The new position.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer position(int position) {
		buffer.position(position);
		return this;
	}

	/**
	 * Places the contents of the specified byte array into this DataBuffer.
	 *
	 * @param bytes The byte array.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer put(byte[] bytes) {
		buffer.put(bytes);
		return this;
	}

	/**
	 * Places the contents of the specified byte array into this DataBuffer, starting from {@code offset} and reading
	 * {@code length} bytes.
	 *
	 * @param bytes The byte array.
	 * @param offset The offset.
	 * @param length The amount of bytes to place.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer put(byte[] bytes, int offset, int length) {
		buffer.put(bytes, offset, length);
		return this;
	}

	/**
	 * Puts a {@link ByteBuffer} into this DataBuffer (see {@link ByteBuffer#put(ByteBuffer)}).
	 *
	 * @param buffer The byte buffer.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer put(ByteBuffer buffer) {
		buffer.put(buffer);
		return this;
	}

	/**
	 * Places the contents of the specified buffer into this DataBuffer.
	 *
	 * @param buffer The DataBuffer..
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer put(DataBuffer buffer) {
		this.buffer.put(buffer.buffer);
		return this;
	}

	/**
	 * Puts an ASCII string into this DataBuffer, terminated with the byte value 10. Used exclusively in old clients.
	 *
	 * @param string The string.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putAsciiString(String string) {
		for (char c : string.toCharArray()) {
			buffer.put((byte) c);
		}

		buffer.put((byte) 10);
		return this;
	}

	/**
	 * Places a {@code boolean} into this Buffer, where a {@code true} value places a {@code byte} of 1, and a
	 * {@code false} value places a {@code byte} of 0.
	 *
	 * @param value The value.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putBoolean(boolean value) {
		buffer.put((byte) (value ? 1 : 0));
		return this;
	}

	/**
	 * Places an {@code int} into this Buffer, as a {@code boolean}, where a non-zero value places a {@code byte} of 1,
	 * and a 0 value places a {@code byte} of 0.
	 *
	 * @param value The value.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putBoolean(int value) {
		return putBoolean(value != 0);
	}

	/**
	 * Puts a byte into the buffer.
	 *
	 * @param value The value.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putByte(int value) {
		buffer.put((byte) value);
		return this;
	}

	/**
	 * Puts a 'c string' into this DataBuffer.
	 *
	 * @param string The string.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putCString(String string) {
		for (char c : string.toCharArray()) {
			buffer.put((byte) c);
		}

		buffer.put((byte) 0);
		return this;
	}

	/**
	 * Puts an int into this DataBuffer.
	 *
	 * @param value The int.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putInt(int value) {
		buffer.putInt(value);
		return this;
	}

	/**
	 * Places an int with the specified value at the specified index.
	 *
	 * @param index The index.
	 * @param value The value.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putInt(int index, int value) {
		buffer.putInt(index, value);
		return this;
	}

	/**
	 * Puts a long into this DataBuffer.
	 *
	 * @param value The long.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putLong(long value) {
		return putInt((int) value >> 32).putInt((int) value);
	}

	/**
	 * Puts a short into this DataBuffer.
	 *
	 * @param value The short.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putShort(int value) {
		buffer.putShort((short) value);
		return this;
	}

	/**
	 * Puts a tri-byte into this DataBuffer.
	 *
	 * @param value The tri-byte.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer putTriByte(int value) {
		buffer.put((byte) (value >> 16));
		buffer.put((byte) (value >> 8));
		buffer.put((byte) value);
		return this;
	}

	/**
	 * Gets the amount of bytes remaining in this DataBuffer (i.e. {@link #limit} - {@link #position}).
	 *
	 * @return The amount of bytes remaining.
	 */
	public int remaining() {
		return buffer.remaining();
	}

	/**
	 * Resets the mark of this DataBuffer.
	 *
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer reset() {
		buffer.reset();
		return this;
	}

	/**
	 * Skips the specified amount of bytes.
	 *
	 * @param bytes The amount of bytes to skip.
	 * @return This DataBuffer, for chaining.
	 */
	public DataBuffer skip(int bytes) {
		buffer.position(buffer.position() + bytes);
		return this;
	}

	/**
	 * Slices this DataBuffer.
	 *
	 * @return The sliced DataBuffer.
	 */
	public DataBuffer slice() {
		return new DataBuffer(buffer.slice());
	}

	/**
	 * {@link Whirlpool}s the data in this DataBuffer.
	 *
	 * @return The whirlpool hash, as a DataBuffer.
	 */
	public DataBuffer whirlpool() {
		return new DataBuffer(Whirlpool.whirlpool(buffer));
	}

}