package rs.emulate.modern.cs.interp.stack;

import java.util.function.Function;

/**
 * Contains stack-related constants.
 *
 * @author Major
 */
final class StackConstants {

	/**
	 * The default capacity of a stack.
	 */
	static final int DEFAULT_SIZE = 16;

	/**
	 * The growth factor of a stack, multiplied with the current stack size to produce the new size of the stack.
	 */
	static final Function<Integer, Integer> GROWER = current -> (int) (current * 1.5);

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private StackConstants() {

	}

}