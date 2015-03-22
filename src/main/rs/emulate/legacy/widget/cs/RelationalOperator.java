package rs.emulate.legacy.widget.cs;

import java.util.Arrays;

/**
 * An operator used when interpreting a {@link LegacyClientScript}.
 *
 * @author Major
 */
enum RelationalOperator {

	/**
	 * The not equal operator.
	 */
	NOT_EQUAL(1, "!="),

	/**
	 * The greater than or equal to operator.
	 */
	GREATER_OR_EQUAL(2, ">="),

	/**
	 * The less than or equal to operator.
	 */
	LESS_OR_EQUAL(3, "<="),

	/**
	 * The equal operator.
	 */
	EQUAL(4, "=");

	/**
	 * Gets the RelationalOperator with the specified integer value.
	 * 
	 * @param value The integer value.
	 * @return The RelationalOperator.
	 * @throws IllegalArgumentException If no RelationalOperator with the specified integer value exists.
	 */
	public static RelationalOperator valueOf(int value) {
		return Arrays.stream(values()).filter(operator -> operator.value == value).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No RelationalOperator with a value of " + value + " exists."));
	}

	/**
	 * The integer value of this RelationalOperator.
	 */
	private final int value;

	/**
	 * The token of this RelationalOperator.
	 */
	private final String token;

	/**
	 * Creates the RelationalOperator.
	 *
	 * @param value The integer value of the RelationalOperator.
	 * @param token The String token of the RelationalOperator.
	 */
	private RelationalOperator(int value, String token) {
		this.value = value;
		this.token = token;
	}

	/**
	 * Gets the String token of this RelationalOperator.
	 * 
	 * @return The token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Gets the integer value of this RelationalOperator.
	 * 
	 * @return The integer value.
	 */
	public int getValue() {
		return value;
	}

}