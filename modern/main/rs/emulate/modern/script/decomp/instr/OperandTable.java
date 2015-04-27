package rs.emulate.modern.script.decomp.instr;

import java.util.Optional;

import com.google.common.base.Preconditions;

/**
 * A table of operands of a script.
 *
 * @author Major
 */
public final class OperandTable {

	/**
	 * The {@code int} operands, wrapped in an Optional.
	 */
	private final Optional<int[]> ints;

	/**
	 * The {@code long} operands, wrapped in an Optional.
	 */
	private final Optional<long[]> longs;

	/**
	 * The String operands, wrapped in an Optional.
	 */
	private final Optional<String[]> strings;

	/**
	 * Creates the OperandList with no operands.
	 */
	public OperandTable() {
		this(Optional.empty(), Optional.empty(), Optional.empty());
	}

	/**
	 * Creates the OperandList with the specified {@code int} operands.
	 *
	 * @param ints The {@code int} operands. Must not be {@code null}.
	 */
	public OperandTable(int[] ints) {
		this(Optional.of(ints), Optional.empty(), Optional.empty());
	}

	/**
	 * Creates the OperandList with the specified {@code int} and {@code long} operands.
	 *
	 * @param ints The {@code int} operands. Must not be {@code null}.
	 * @param longs The {@code long} operands. Must not be {@code null}.
	 */
	public OperandTable(int[] ints, long[] longs) {
		this(Optional.of(ints), Optional.of(longs), Optional.empty());
		Preconditions.checkArgument(ints.length == longs.length, "Operand arrays must be of equal length.");
	}

	/**
	 * Creates the OperandList with the specified operands.
	 *
	 * @param ints The {@code int} operands. Must not be {@code null}.
	 * @param longs The {@code long} operands. Must not be {@code null}.
	 * @param strings The String operands. Must not be {@code null}.
	 */
	public OperandTable(int[] ints, long[] longs, String[] strings) {
		this(Optional.of(ints), Optional.of(longs), Optional.of(strings));

		Preconditions.checkArgument(ints.length == longs.length && ints.length == strings.length,
				"Operand arrays must be of equal length.");
	}

	/**
	 * Creates the OperandList with the specified {@code int} and String operands.
	 *
	 * @param ints The {@code int} operands. Must not be {@code null}.
	 * @param strings The String operands. Must not be {@code null}.
	 */
	public OperandTable(int[] ints, String[] strings) {
		this(Optional.of(ints), Optional.empty(), Optional.of(strings));
		Preconditions.checkArgument(ints.length == strings.length, "Operand arrays must be of equal length.");
	}

	/**
	 * Creates the OperandList with the specified {@code long} operands.
	 *
	 * @param longs The {@code long} operands. Must not be {@code null}.
	 */
	public OperandTable(long[] longs) {
		this(Optional.empty(), Optional.of(longs), Optional.empty());
	}

	/**
	 * Creates the OperandList with the specified {@code long} and String operands.
	 *
	 * @param longs The {@code long} operands. Must not be {@code null}.
	 * @param strings The String operands. Must not be {@code null}.
	 */
	public OperandTable(long[] longs, String[] strings) {
		this(Optional.empty(), Optional.of(longs), Optional.of(strings));
		Preconditions.checkArgument(longs.length == strings.length, "Operand arrays must be of equal length.");
	}

	/**
	 * Creates the OperandList with the specified String operands.
	 *
	 * @param strings The String operands. Must not be {@code null}.
	 */
	public OperandTable(String[] strings) {
		this(Optional.empty(), Optional.empty(), Optional.of(strings));
	}

	/**
	 * Creates the OperandList with the specified operands.
	 *
	 * @param ints The {@code int} operands, wrapped in an Optional. Must not be {@code null}.
	 * @param longs The {@code long} operands, wrapped in an Optional. Must not be {@code null}.
	 * @param strings The String operands, wrapped in an Optional. Must not be {@code null}.
	 */
	private OperandTable(Optional<int[]> ints, Optional<long[]> longs, Optional<String[]> strings) {
		this.ints = ints;
		this.longs = longs;
		this.strings = strings;
	}

	/**
	 * Gets an {@code int} operand at the specified index.
	 * 
	 * @param index The index of the operand.
	 * @return The operand.
	 */
	public int getIntOperand(int index) {
		if (!ints.isPresent()) {
			throw new IllegalArgumentException("Cannot get an int operand from a script with no integer operands.");
		}

		int[] array = ints.get();
		Preconditions.checkElementIndex(index, array.length, "Integer operand index out of bounds.");

		return array[index];
	}

	/**
	 * Gets a {@code long} operand at the specified index.
	 * 
	 * @param index The index of the operand.
	 * @return The operand.
	 */
	public long getLongOperand(int index) {
		if (!ints.isPresent()) {
			throw new IllegalArgumentException("Cannot get an int operand from a script with no integer operands.");
		}

		long[] array = longs.get();
		Preconditions.checkElementIndex(index, array.length, "Integer operand index out of bounds.");

		return array[index];
	}

	/**
	 * Gets a String operand at the specified index.
	 * 
	 * @param index The index of the operand.
	 * @return The operand.
	 */
	public String getStringOperand(int index) {
		if (!strings.isPresent()) {
			throw new IllegalArgumentException("Cannot get an int operand from a script with no integer operands.");
		}

		String[] array = strings.get();
		Preconditions.checkElementIndex(index, array.length, "Integer operand index out of bounds.");

		return array[index];
	}

}