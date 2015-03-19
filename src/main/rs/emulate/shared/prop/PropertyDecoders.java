package rs.emulate.shared.prop;

import java.util.function.Function;

import rs.emulate.shared.util.DataBuffer;

/**
 * Contains static {@link Function}s to decode values.
 * 
 * @author Major
 */
public final class PropertyDecoders {

	/**
	 * A {@link Function} that decodes a byte from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> BYTE_DECODER = DataBuffer::getUnsignedByte;

	/**
	 * A {@link Function} that reads no data from the {@link DataBuffer} and simply returns {@code false}.
	 */
	public static final Function<DataBuffer, Boolean> FALSE_DECODER = buffer -> false;

	/**
	 * A {@link Function} that decodes an integer from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> INT_DECODER = DataBuffer::getInt;

	/**
	 * A {@link Function} that decodes a short from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SHORT_DECODER = DataBuffer::getUnsignedShort;

	/**
	 * A {@link Function} that decodes a signed byte from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SIGNED_BYTE_DECODER = DataBuffer::getByte;

	/**
	 * A {@link Function} that decodes a signed short from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SIGNED_SHORT_DECODER = DataBuffer::getShort;

	/**
	 * A {@link Function} that decodes a smart from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SMART_DECODER = DataBuffer::getSmart;

	/**
	 * A {@link Function} that decodes an old-style (10 terminated) string from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, String> STRING_DECODER = DataBuffer::getString;

	/**
	 * A {@link Function} that decodes a tri-byte from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> TRI_BYTE_DECODER = DataBuffer::getUnsignedTriByte;

	/**
	 * A {@link Function} that reads no data from the {@link DataBuffer} and simply returns {@code true}.
	 */
	public static final Function<DataBuffer, Boolean> TRUE_DECODER = buffer -> true;

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private PropertyDecoders() {

	}

}