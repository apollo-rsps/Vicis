package rs.emulate.legacy.graphics;

import java.util.Arrays;

/**
 * A format a sprite or image may be encoded in.
 * 
 * @author Major
 */
public enum ImageFormat {

	/**
	 * The format where pixels are ordered by column (e.g. array[1] is the pixel at (0, 1), etc).
	 */
	COLUMN_ORDERED(0),

	/**
	 * The format where pixels are ordered by row (e.g. array[1] is the pixel at (1, 0), etc).
	 */
	ROW_ORDERED(1);

	/**
	 * Returns the {@link ImageFormat} with the specified integer value.
	 * 
	 * @param value The integer value.
	 * @return The format.
	 * @throws IllegalArgumentException If no format with the specified integer value exists.
	 */
	public static ImageFormat valueOf(int value) {
		return Arrays.stream(values()).filter(format -> format.value == value).findAny()
				.orElseThrow(() -> new IllegalArgumentException("Invalid integer value of " + value + " specified."));
	}

	/**
	 * The integer value of this format.
	 */
	private final int value;

	/**
	 * Creates the ImageFormat.
	 * 
	 * @param value The integer value.
	 */
	private ImageFormat(int value) {
		this.value = value;
	}

	/**
	 * Gets the integer value of this format.
	 * 
	 * @return The integer value.
	 */
	public int getValue() {
		return value;
	}

}