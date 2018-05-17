package rs.emulate.modern.script.interp.stack

import java.util.Arrays
import java.util.NoSuchElementException

/**
 * A simple stack of `long`s, implemented specifically to avoid the boxing overhead from generic types.
 *
 * @param capacity The initial capacity of the stack.
 */
class LongStack(capacity: Int = StackConstants.DEFAULT_SIZE) {

    /**
     * The pointer to the next position to insert an element in the stack.
     */
    private var pointer: Int = 0

    /**
     * The stack of `long`s.
     */
    private var stack: LongArray = LongArray(capacity)

    /**
     * The size of this LongStack.
     */
    val size: Int
        get() {
            return pointer
        }

    /**
     * Pops an element from the stack.
     */
    fun pop(): Long {
        if (pointer == 0) {
            throw NoSuchElementException("Cannot pop from an empty stack.")
        }

        return stack[--pointer]
    }

    /**
     * Pushes a value onto this stack.
     */
    fun push(value: Long) {
        if (stack.size == size) {
            grow()
        }

        stack[pointer++] = value
    }

    override fun equals(other: Any?): Boolean {
        if (other is LongStack) {
            return pointer == other.pointer && Arrays.equals(stack, other.stack)
        }

        return false
    }

    override fun hashCode(): Int {
        return 31 * pointer + Arrays.hashCode(stack)
    }

    override fun toString(): String {
        return "LongStack(pointer=$pointer, stack=${Arrays.toString(stack)})"
    }

    private fun grow() {
        stack = Arrays.copyOf(stack, StackConstants.GROWER(stack.size))
    }

}
