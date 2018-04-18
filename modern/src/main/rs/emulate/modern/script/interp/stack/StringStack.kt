package rs.emulate.modern.script.interp.stack

import java.util.Arrays
import java.util.NoSuchElementException

/**
 * A stack of Strings. This is used instead of the existing Stack class in the Java API because synchronization
 * is not required, and instead of an ArrayDeque as that includes functionality that ClientScripts do not support (such
 * as insertion at the head).
 *
 * @param capacity The initial capacity of the stack.
 */
class StringStack(capacity: Int = StackConstants.DEFAULT_SIZE) {

    /**
     * The pointer to the next position to insert an element in the stack.
     */
    private var pointer: Int = 0

    /**
     * The stack of Strings.
     */
    private var stack: Array<String?> = arrayOfNulls(capacity)

    /**
     * The size of this StringStack.
     */
    val size: Int
        get() {
            return pointer
        }

    /**
     * Pops an element from the stack.
     */
    fun pop(): String {
        if (pointer == 0) {
            throw NoSuchElementException("Cannot pop from an empty stack.")
        }

        try {
            return stack[--pointer]!!
        } finally {
            stack[pointer] = null
        }
    }

    /**
     * Pushes a value onto this stack.
     */
    fun push(value: String) {
        if (stack.size == size) {
            grow()
        }

        stack[pointer++] = value
    }

    override fun equals(other: Any?): Boolean {
        if (other is StringStack) {
            return pointer == other.pointer && Arrays.equals(stack, other.stack)
        }

        return false
    }

    override fun hashCode(): Int {
        return 31 * pointer + Arrays.hashCode(stack)
    }

    override fun toString(): String {
        return "StringStack(pointer=$pointer, stack=${Arrays.toString(stack)})"
    }

    private fun grow() {
        stack = Arrays.copyOf(stack, StackConstants.GROWER(stack.size))
    }

}
