package rs.emulate.modern.script.interp.stack;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A simple stack of {@code long}s, implemented specifically to avoid the boxing overhead from generic types.
 *
 * @author Major
 */
public final class LongStack {

	/**
	 * The pointer to the next position to insert an element in the stack.
	 */
	private int pointer;

	/**
	 * The stack of {@code long}s.
	 */
	private long[] stack;

	/**
	 * Creates the stack with an initial capacity of {@link StackConstants#DEFAULT_SIZE}.
	 */
	public LongStack() {
		this(StackConstants.DEFAULT_SIZE);
	}

	/**
	 * Creates the LongStack with the specified initial capacity.
	 *
	 * @param capacity The initial capacity of the stack.
	 */
	public LongStack(int capacity) {
		stack = new long[capacity];
	}

	/**
	 * Returns whether or not the specified LongStack is equal to this LongStack.
	 * 
	 * @param other The LongStack to compare with.
	 * @return {@code true} if the specified LongStack is equal to this, {@code false} if not.
	 */
	public boolean equals(LongStack other) {
		if (pointer != other.pointer || size() != other.size()) {
			return false;
		}

		return Arrays.equals(stack, other.stack);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LongStack) {
			return equals((LongStack) obj);
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return (int) Arrays.stream(stack).reduce(prime * stack.length + pointer, (index, result) -> result * prime + index);
	}

	/**
	 * Pops an element from the stack.
	 * 
	 * @return The popped element.
	 * @throws NoSuchElementException If the stack is empty.
	 */
	public long pop() {
		if (pointer == 0) {
			throw new NoSuchElementException("Cannot pop from an empty stack.");
		}

		return stack[--pointer];
	}

	/**
	 * Pushes a value onto this stack.
	 * 
	 * @param value The value.
	 */
	public void push(long value) {
		if (stack.length == size()) {
			grow();
		}

		stack[pointer++] = value;
	}

	/**
	 * Gets the size of this LongStack.
	 * 
	 * @return The size.
	 */
	public int size() {
		return pointer;
	}

	/**
	 * Grows this array.
	 */
	private void grow() {
		stack = Arrays.copyOf(stack, StackConstants.GROWER.apply(stack.length));
	}

}