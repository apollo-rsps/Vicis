package rs.emulate.modern.script.interp.stack;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A simple stack of Strings. This is used instead of the existing Stack class in the Java API because synchronization
 * is not required, and instead of an ArrayDeque as that supplies unwanted functionality (such as insertion at the
 * head)
 * which ClientScripts do not support.
 *
 * @author Major
 */
public final class StringStack {

	/**
	 * The pointer to the next position to insert an element in the stack.
	 */
	private int pointer;

	/**
	 * The stack of Strings.
	 */
	private String[] stack;

	/**
	 * Creates the stack with an initial capacity of {@link StackConstants#DEFAULT_SIZE}.
	 */
	public StringStack() {
		this(StackConstants.DEFAULT_SIZE);
	}

	/**
	 * Creates the StringStack with the specified initial capacity.
	 *
	 * @param capacity The initial capacity of the stack.
	 */
	public StringStack(int capacity) {
		stack = new String[capacity];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StringStack) {
			return equals((StringStack) obj);
		}

		return false;
	}

	/**
	 * Returns whether or not the specified StringStack is equal to this StringStack.
	 *
	 * @param other The StringStack to compare with.
	 * @return {@code true} if the specified StringStack is equal to this, {@code false} if not.
	 */
	public boolean equals(StringStack other) {
		if (pointer != other.pointer || size() != other.size()) {
			return false;
		}

		return Arrays.equals(stack, other.stack);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return Arrays.stream(stack).reduce(0, (result, string) -> result * prime + string.hashCode(), (a, b) -> a + b);
	}

	/**
	 * Pops an element from the stack.
	 *
	 * @return The popped element.
	 * @throws NoSuchElementException If the stack is empty.
	 */
	public String pop() {
		if (pointer == 0) {
			throw new NoSuchElementException("Cannot pop from an empty stack.");
		}

		try {
			return stack[--pointer];
		} finally {
			stack[pointer] = null;
		}
	}

	/**
	 * Pushes a value onto this stack.
	 *
	 * @param value The value.
	 */
	public void push(String value) {
		if (stack.length == size()) {
			grow();
		}

		stack[pointer++] = value;
	}

	/**
	 * Gets the size of this StringStack.
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