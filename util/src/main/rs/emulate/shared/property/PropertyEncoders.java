package rs.emulate.shared.property;

import rs.emulate.shared.util.DataBuffer;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

/**
 * Contains static {@link BiConsumer}s to encode values.
 *
 * @author Major
 */
public final class PropertyEncoders {

	/**
	 * A {@link BiConsumer} to encode a String into a byte array, terminated with the byte value 10.
	 */
	public static final BiConsumer<DataBuffer, String> ASCII_STRING_ENCODER = (buffer, value) -> buffer
			.putAsciiString(value);

	/**
	 * A {@link BiConsumer} to encode a {@link ByteBuffer}
	 */
	public static final BiConsumer<DataBuffer, ByteBuffer> BYTE_BUFFER_ENCODER = (buffer, value) -> buffer.put(value);

	/**
	 * A {@link BiConsumer} to encode a byte.
	 */
	public static final BiConsumer<DataBuffer, Integer> BYTE_ENCODER = (buffer, value) -> buffer.putByte(value);

	/**
	 * A {@link BiConsumer} to encode a null-terminated, C-style String.
	 */
	public static final BiConsumer<DataBuffer, String> C_STRING_ENCODER = (buffer, value) -> buffer.putCString(value);

	/**
	 * A {@link BiConsumer} to encode an integer.
	 */
	public static final BiConsumer<DataBuffer, Integer> INT_ENCODER = (buffer, value) -> buffer.putInt(value);

	/**
	 * A {@link BiConsumer} to encode a long.
	 */
	public static final BiConsumer<DataBuffer, Long> LONG_ENCODER = (buffer, value) -> buffer.putLong(value);

	/**
	 * A {@link BiConsumer} to encode a short.
	 */
	public static final BiConsumer<DataBuffer, Integer> SHORT_ENCODER = (buffer, value) -> buffer.putShort(value);

	/**
	 * A {@link BiConsumer} to encode a tri-byte.
	 */
	public static final BiConsumer<DataBuffer, Integer> TRI_BYTE_ENCODER = (buffer, value) -> buffer.putTriByte(value);

	/**
	 * Gets a {@link BiConsumer} that acts as a disposer, writing the opcode of the property but no data. This is a
	 * method rather than a constant because type inference is required.
	 *
	 * @return The BiConsumer.
	 */
	public static <T> BiConsumer<DataBuffer, T> nullEncoder() {
		return (buffer, value) -> {
		};
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private PropertyEncoders() {

	}

}