package rs.emulate.modern.cs.interp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

import rs.emulate.modern.cs.decomp.instr.OperandTable;
import rs.emulate.modern.cs.interp.stack.IntStack;
import rs.emulate.modern.cs.interp.stack.LongStack;

/**
 * A context for a ClientScript.
 *
 * @author Major
 */
public final class ScriptContext {

	/**
	 * The program counter for this script.
	 */
	private int counter;

	/**
	 * The stack of {@code int}s.
	 */
	private final IntStack ints = new IntStack();

	/**
	 * The stack of {@code long}s.
	 */
	private final LongStack longs = new LongStack();

	/**
	 * The table of operands.
	 */
	private final OperandTable operands;

	/**
	 * The stack of Strings.
	 */
	private final Deque<String> strings = new ArrayDeque<>();

	/**
	 * Creates the ScriptContext.
	 *
	 * @param operands The list of operands.
	 */
	public ScriptContext(OperandTable operands) {
		this.operands = operands;
	}

	/**
	 * Performs a branch, incrementing the {@code counter} by the current {@code int} operand.
	 */
	public void branch() {
		counter += operands.getIntOperand(counter);
	}

	/**
	 * Executes a branch if the specified {@code condition} is {@code true}.
	 * <p>
	 * This is a utility method for the variety of branching statements used by ClientScripts.
	 * 
	 * @param condition Whether or not to execute the branch.
	 */
	public void branchIf(boolean condition) {
		if (condition) {
			branch();
		}
	}

	/**
	 * Gets the program counter.
	 * 
	 * @return The program counter.
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Gets the {@code int} operand at the current {@code counter} index.
	 * 
	 * @return The {@code int} operand.
	 */
	public int getIntOperand() {
		return operands.getIntOperand(counter);
	}

	/**
	 * Gets the {@code int} operand at the specified index.
	 * 
	 * @param index The index.
	 * @return The {@code int} operand.
	 */
	public int getIntOperand(int index) {
		return operands.getIntOperand(index);
	}

	/**
	 * Gets the {@code long} operand at the current {@code counter} index.
	 * 
	 * @return The {@code long} operand.
	 */
	public long getLongOperand() {
		return operands.getLongOperand(counter);
	}

	/**
	 * Gets the {@code long} operand at the specified index.
	 * 
	 * @param index The index.
	 * @return The {@code long} operand.
	 */
	public long getLongOperand(int index) {
		return operands.getLongOperand(index);
	}

	/**
	 * Gets the String operand at the current {@code counter} index.
	 * 
	 * @return The String operand.
	 */
	public String getStringOperand() {
		return operands.getStringOperand(counter);
	}

	/**
	 * Gets the String operand at the specified index.
	 * 
	 * @param index The index.
	 * @return The String operand.
	 */
	public String getStringOperand(int index) {
		return operands.getStringOperand(index);
	}

	/**
	 * Increments the program counter by 1.
	 */
	public void incrementCounter() {
		counter++;
	}

	/**
	 * Increments the program counter by the specified amount.
	 * 
	 * @param amount The amount.
	 */
	public void incrementCounter(int amount) {
		counter += amount;
	}

	/**
	 * Gets the amount of {@code int}s in the stack.
	 * 
	 * @return The amount.
	 */
	public int intCount() {
		return ints.size();
	}

	/**
	 * Gets the amount of {@code long}s in the stack.
	 * 
	 * @return The amount.
	 */
	public int longCount() {
		return longs.size();
	}

	/**
	 * Pops an {@code int} from the stack.
	 * 
	 * @return The {@code int}.
	 */
	public int popInt() {
		return ints.pop();
	}

	/**
	 * Pops a {@code long} from the stack.
	 * 
	 * @return The {@code long}.
	 */
	public long popLong() {
		return longs.pop();
	}

	/**
	 * Pops a String from the stack.
	 * 
	 * @return The String.
	 */
	public String popString() {
		return strings.pop();
	}

	/**
	 * Pushes an {@code int} onto the stack.
	 * 
	 * @param value The {@code int}.
	 */
	public void pushInt(int value) {
		ints.push(value);
	}

	/**
	 * Pushes a {@code long} onto the stack.
	 * 
	 * @param value The {@code long}.
	 */
	public void pushLong(long value) {
		longs.push(value);
	}

	/**
	 * Pushes a String onto the stack.
	 * 
	 * @param value The String.
	 */
	public void pushString(String value) {
		strings.push(value);
	}

	/**
	 * Sets the value of the program counter.
	 * 
	 * @param counter The new value.
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * Gets the amount of Strings in the stack.
	 * 
	 * @return The amount.
	 */
	public int stringCount() {
		return strings.size();
	}

	/**
	 * Gets the stack of {@code int}s.
	 * <p>
	 * This method does <strong>not</strong> create a copy of the stack, and is intended only for use by
	 * {@link InstructionMap}.
	 * 
	 * @return The IntStack.
	 */
	IntStack getInts() {
		return ints;
	}

	/**
	 * Gets the stack of {@code long}s.
	 * <p>
	 * This method does <strong>not</strong> create a copy of the stack, and is intended only for use by
	 * {@link InstructionMap}.
	 *
	 * @return The LongStack.
	 */
	LongStack getLongs() {
		return longs;
	}

	/**
	 * Gets the stack of Strings.
	 * <p>
	 * This method does <strong>not</strong> create a copy of the stack, and is intended only for use by
	 * {@link InstructionMap}.
	 *
	 * @return The stack of Strings.
	 */
	Deque<String> getStrings() {
		return strings;
	}

	/**
	 * Gets a section of the stack of Strings, as a {@link Stream}.
	 * 
	 * @param amount The amount of Strings to skip.
	 * @return The Stream of Strings.
	 */
	Stream<String> getStrings(int amount) {
		return strings.stream().skip(amount);
	}

}