package rs.emulate.modern.script.interp.stack;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A simple stack of {@code int}s, implemented specifically to avoid the boxing overhead from generic types.
 *
 * @author Major
 */
public final class IntStack {

	/**
	 * The pointer to the next position to insert an element in the stack.
	 */
	private int pointer;

	/**
	 * The stack of {@code int}s.
	 */
	private int[] stack;

	/**
	 * Creates the stack with an initial capacity of {@link StackConstants#DEFAULT_SIZE}.
	 */
	public IntStack() {
		this(StackConstants.DEFAULT_SIZE);
	}

	/**
	 * Creates the IntStack with the specified initial capacity.
	 *
	 * @param capacity The initial capacity of the stack.
	 */
	public IntStack(int capacity) {
		stack = new int[capacity];
	}

	/**
	 * Returns whether or not the specified IntStack is equal to this IntStack.
	 *
	 * @param other The IntStack to compare with.
	 * @return {@code true} if the specified IntStack is equal to this, {@code false} if not.
	 */
	public boolean equals(IntStack other) {
		if (pointer != other.pointer || size() != other.size()) {
			return false;
		}

		return Arrays.equals(stack, other.stack);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntStack) {
			return equals((IntStack) obj);
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return Arrays.stream(stack).reduce(prime * stack.length + pointer, (index, result) -> result * prime + index);
	}

	/**
	 * Pops an element from the stack.
	 *
	 * @return The popped element.
	 * @throws NoSuchElementException If the stack is empty.
	 */
	public int pop() {
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
	public void push(int value) {
		if (stack.length == size()) {
			grow();
		}

		stack[pointer++] = value;
	}

	/**
	 * Gets the size of this IntStack.
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