package rs.emulate.shared.property;

import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.CharMatcher;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * Contains parser {@link Function}s for {@link Property} values.
 *
 * @author Major
 */
public final class PropertyParsers {

	/**
	 * Returns a parser {@link Function} for an ASCII String.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<String>> forAsciiString() {
		return input -> input != null && CharMatcher.ASCII.matchesAllOf(input) ? Optional.of(input) : Optional.empty();
	}

	/**
	 * Returns a parser {@link Function} for a {@code boolean}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Boolean>> forBoolean() {
		return input -> {
			if ("true".equals(input)) {
				return Optional.of(true);
			} else if ("false".equals(input)) {
				return Optional.of(false);
			}

			return Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code byte}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> signedByte() {
		return input -> {
			Integer value = Ints.tryParse(input);
			return (value != null && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) ? Optional.of(value)
					: Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code int}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> signedInt() {
		return input -> {
			Integer value = Ints.tryParse(input);
			return (value != null && value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) ? Optional.of(value)
					: Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code short}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> signedShort() {
		return input -> {
			Integer value = Ints.tryParse(input);
			return (value != null && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) ? Optional.of(value)
					: Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for a signed tri-byte (24 bit value).
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> signedTriByte() {
		final int minimum = -(1 << (3 * Byte.SIZE));
		final int maximum = (1 << (3 * Byte.SIZE - 1)) - 1;

		return input -> {
			Integer value = Ints.tryParse(input);

			return (value != null && value >= minimum && value <= maximum) ? Optional.of(value) : Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code unsigned byte}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> unsignedByte() {
		return input -> {
			Integer value = Ints.tryParse(input);
			return (value != null && value >= 0 && value <= 1 << Byte.SIZE) ? Optional.of(value) : Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code unsigned int}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Long>> unsignedInt() {
		return input -> {
			Long value = Longs.tryParse(input);
			return (value != null && value >= 0 && value <= 1 << Integer.SIZE) ? Optional.of(value) : Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an {@code unsigned short}.
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> unsignedShort() {
		return input -> {
			Integer value = Ints.tryParse(input);
			return (value != null && value >= 0 && value <= 1 << Short.SIZE) ? Optional.of(value) : Optional.empty();
		};
	}

	/**
	 * Returns a parser {@link Function} for an unsigned tri-byte (24 bit value).
	 *
	 * @return The Function.
	 */
	public static Function<String, Optional<Integer>> unsignedTriByte() {
		return input -> {
			Integer value = Ints.tryParse(input);

			return (value != null && value >= 0 && value <= 1 << (3 * Byte.SIZE)) ? Optional.of(value) : Optional
					.empty();
		};
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private PropertyParsers() {

	}

}