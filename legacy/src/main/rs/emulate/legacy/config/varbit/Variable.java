package rs.emulate.legacy.config.varbit;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * A variable used in a definition, with a variable id, a high bit mask index, and a low bit mask index.
 *
 * @author Major
 */
public final class Variable {

	/**
	 * The empty Variable used as the default value.
	 */
	public static final Variable EMPTY = new Variable();

	/**
	 * Returns whether or not the specified index is valid for a mask.
	 *
	 * @param index The index.
	 * @return {@code true} if {@code 0 <= index <= 32}, {@code false} if not.
	 */
	public static final boolean validMaskIndex(int index) {
		return index >= 0 && index < 33;
	}

	/**
	 * The high bit mask index.
	 */
	private final int high;

	/**
	 * The low bit mask index.
	 */
	private final int low;

	/**
	 * The variable id.
	 */
	private final int variable;

	/**
	 * Creates the Variable.
	 *
	 * @param variable The variable id.
	 * @param high The high bit mask index.
	 * @param low The low bit mask index.
	 */
	public Variable(int variable, int high, int low) {
		Preconditions.checkArgument(variable >= 0, "Variable id must be greater than or equal to 0.");
		Preconditions.checkArgument(validMaskIndex(high) && validMaskIndex(low),
				"High and low mask indices must be greater than or equal to 0 and less than 33.");
		Preconditions.checkArgument(high >= low, "High bit mask must be greater than the low bit mask.");

		this.variable = variable;
		this.high = high;
		this.low = low;
	}

	/**
	 * Creates an empty Variable with {@code -1} for each value.
	 */
	private Variable() { // Public constructor will throw an exception for values of -1.
		variable = high = low = -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Variable) {
			Variable other = (Variable) obj;
			return high == other.high && low == other.low && variable == other.variable;
		}
		return false;
	}

	/**
	 * Gets the high bit mask index.
	 *
	 * @return The high bit mask index.
	 */
	public int getHigh() {
		return high;
	}

	/**
	 * Gets the low bit mask index.
	 *
	 * @return The low bit mask index.
	 */
	public int getLow() {
		return low;
	}

	/**
	 * Gets the variable id.
	 *
	 * @return The variable id.
	 */
	public int getVariable() {
		return variable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + high;
		result = prime * result + low;
		return prime * result + variable;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("variable", variable).add("high bit", high).add("low bit", low).toString();
	}

}