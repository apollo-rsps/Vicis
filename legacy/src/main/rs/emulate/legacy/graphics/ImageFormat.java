package rs.emulate.legacy.graphics;

import java.util.Arrays;

/**
 * A format an image may be encoded in.
 * 
 * @author Major
 */
public enum ImageFormat {

	/**
	 * The format where pixels are ordered by column (e.g. {@code array[1]} is the pixel at (0, 1)).
	 */
	COLUMN_ORDERED(0),

	/**
	 * The format where pixels are ordered by row (e.g. {@code array[1]} is the pixel at (1, 0)).
	 */
	ROW_ORDERED(1);

	/**
	 * Returns the {@link ImageFormat} with the specified integer value.
	 * 
	 * @param value The integer value.
	 * @return The ImageFormat.
	 * @throws IllegalArgumentException If no ImageFormat with the specified integer value exists.
	 */
	public static ImageFormat valueOf(int value) {
		return Arrays.stream(values()).filter(format -> format.value == value).findAny()
				.orElseThrow(() -> new IllegalArgumentException("Invalid integer value of " + value + " specified."));
	}

	/**
	 * The integer value of this ImageFormat.
	 */
	private final int value;

	/**
	 * Creates the ImageFormat.
	 * 
	 * @param value The integer value of the ImageFormat.
	 */
	private ImageFormat(int value) {
		this.value = value;
	}

	/**
	 * Gets the integer value of this ImageFormat.
	 * 
	 * @return The integer value.
	 */
	public int toInteger() {
		return value;
	}

}