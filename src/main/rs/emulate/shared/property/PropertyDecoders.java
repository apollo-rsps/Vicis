package rs.emulate.shared.property;

import java.util.function.Function;

import rs.emulate.shared.util.DataBuffer;

/**
 * Contains static {@link Function}s to decode values.
 * 
 * @author Major
 */
public final class PropertyDecoders {

	/**
	 * A {@link Function} that decodes an unsigned {@code byte} from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> BYTE_DECODER = DataBuffer::getUnsignedByte;

	/**
	 * A {@link Function} that reads no data from the {@link DataBuffer} and simply returns {@code false}.
	 */
	public static final Function<DataBuffer, Boolean> FALSE_DECODER = buffer -> false;

	/**
	 * A {@link Function} that decodes an unsigned {@code int} from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> INT_DECODER = DataBuffer::getInt;

	/**
	 * A {@link Function} that decodes an unsigned {@code short} from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SHORT_DECODER = DataBuffer::getUnsignedShort;

	/**
	 * A {@link Function} that decodes a signed {@code byte} from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SIGNED_BYTE_DECODER = DataBuffer::getByte;

	/**
	 * A {@link Function} that decodes a signed {@code short} from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SIGNED_SHORT_DECODER = DataBuffer::getShort;

	/**
	 * A {@link Function} that decodes a smart from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, Integer> SMART_DECODER = DataBuffer::getSmart;

	/**
	 * A {@link Function} that decodes an old-style (10-terminated) String from the specified {@link DataBuffer}.
	 */
	public static final Function<DataBuffer, String> ASCII_STRING_DECODER = DataBuffer::getString;

	/**
	 * A {@link Function} that decodes an unsigned {@code tri-byte} from the specified {@link DataBuffer}.
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