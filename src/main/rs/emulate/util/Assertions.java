package rs.emulate.util;

import java.util.Arrays;
import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * Contains utility methods similar to (and utilising) Guava's {@link Preconditions}.
 * 
 * @author Major
 */
public final class Assertions {

	/**
	 * Verifies that the value of the specified long can be stored in a byte.
	 * 
	 * @param value The integer value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the value cannot be stored in a byte.
	 */
	public static void checkByte(long value, String message) {
		Preconditions.checkArgument(value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE, message);
	}

	/**
	 * Verifies that the value of the specified long can be stored in an int.
	 * 
	 * @param value The long value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the value cannot be stored in an int.
	 */
	public static void checkInt(long value, String message) {
		Preconditions.checkArgument(value >= Integer.MIN_VALUE && value <= Integer.MIN_VALUE, message);
	}

	/**
	 * Checks that the specified value is negative (i.e. {@code < 0}).
	 * 
	 * @param value The value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the specified value is not negative.
	 */
	public static void checkNegative(long value, String message) {
		Preconditions.checkArgument(value < 0, message);
	}

	/**
	 * Checks that the specified string is neither {@code null} nor {@link String#isEmpty() empty}.
	 * 
	 * @param string The string to check.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the string is {@code null} or {@link String#isEmpty() empty}.
	 */
	public static void checkNonEmpty(String string, String message) {
		Preconditions.checkArgument(string != null && !string.isEmpty(), message);
	}

	/**
	 * Checks that the specified value is non-negative (i.e. {@code >= 0}).
	 * 
	 * @param value The value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the specified value is negative.
	 */
	public static void checkNonNegative(long value, String message) {
		Preconditions.checkArgument(value >= 0, message);
	}

	/**
	 * Checks that each of the specified objects is non-null.
	 * 
	 * @param message The message of the exception thrown if the check fails.
	 * @param objects The objects to check.
	 * @throws NullPointerException If any of the specified objects are null.
	 */
	public static void checkNonNull(String message, Object... objects) {
		if (objects == null || !Arrays.stream(objects).allMatch(Objects::nonNull)) {
			throw new NullPointerException(message);
		}
	}

	/**
	 * Checks that the specified value is non-positive (i.e. {@code <= 0}).
	 * 
	 * @param value The value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the specified value is positive.
	 */
	public static void checkNonPositive(long value, String message) {
		Preconditions.checkArgument(value <= 0, message);
	}

	/**
	 * Checks that the specified value is positive (i.e. {@code > 0}).
	 * 
	 * @param value The value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the specified value is not positive.
	 */
	public static void checkPositive(long value, String message) {
		Preconditions.checkArgument(value > 0, message);
	}

	/**
	 * Verifies that the value of the specified long can be stored in a short.
	 * 
	 * @param value The long value.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If the value cannot be stored in a short.
	 */
	public static void checkShort(long value, String message) {
		Preconditions.checkArgument(value >= Short.MIN_VALUE && value <= Short.MAX_VALUE, message);
	}

	/**
	 * Checks that the specified value is between the {@code start} and {@code end} values, inclusive (i.e.
	 * {@code start <= value <= end}.
	 * 
	 * @param start The start value.
	 * @param end The end value.
	 * @param value The value to check.
	 * @param message The message of the exception thrown if the check fails.
	 * @throws IllegalArgumentException If {@code end} is less than {@code start}, or if the check fails.
	 */
	public static void checkWithin(int start, int end, int value, String message) {
		Preconditions.checkArgument(start <= end, "End value must be greater than or equal to start value.");
		Preconditions.checkArgument(value >= start && value <= end, message);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Assertions() {

	}

}