package rs.emulate.modern.script.interp

import rs.emulate.modern.script.decomp.instr.OperandTable
import rs.emulate.modern.script.interp.stack.IntStack
import rs.emulate.modern.script.interp.stack.LongStack
import java.util.ArrayDeque
import java.util.stream.Stream

/**
 * A context for a ClientScript.
 *
 * @param operands The [OperandTable].
 */
class ScriptContext(private val operands: OperandTable) {

    internal val ints = IntStack()

    internal val longs = LongStack()

    internal val strings = ArrayDeque<String>()

    /**
     * The program counter for the script.
     */
    var counter: Int = 0

    /**
     * Gets the `int` operand at the current `counter` index.
     */
    val intOperand: Int
        get() = operands.getInt(counter)

    /**
     * Gets the `long` operand at the current `counter` index.
     */
    val longOperand: Long
        get() = operands.getLong(counter)

    /**
     * Gets the String operand at the current `counter` index.
     */
    val stringOperand: String
        get() = operands.getString(counter)

    /**
     * Performs a branch, incrementing the `counter` by the current `int` operand.
     */
    fun branch() {
        counter += operands.getInt(counter)
    }

    /**
     * Executes a branch if the specified `condition` is `true`.
     *
     * This is a utility method for the variety of branching statements used by ClientScripts.
     */
    fun branchIf(condition: Boolean) {
        if (condition) {
            branch()
        }
    }

    /**
     * Gets the `int` operand at the specified index.
     */
    fun getIntOperand(index: Int): Int {
        return operands.getInt(index)
    }

    /**
     * Gets the `long` operand at the specified index.
     */
    fun getLongOperand(index: Int): Long {
        return operands.getLong(index)
    }

    /**
     * Gets the String operand at the specified index.
     */
    fun getStringOperand(index: Int): String {
        return operands.getString(index)
    }

    /**
     * Increments the program counter by 1.
     */
    fun incrementCounter() {
        counter++
    }

    /**
     * Increments the program counter by the specified amount.
     */
    fun incrementCounter(amount: Int) {
        counter += amount
    }

    /**
     * Gets the amount of `int`s in the stack.
     */
    fun intCount(): Int {
        return ints.size
    }

    /**
     * Gets the amount of `long`s in the stack.
     */
    fun longCount(): Int {
        return longs.size
    }

    /**
     * Pops an `int` from the stack.
     */
    fun popInt(): Int {
        return ints.pop()
    }

    /**
     * Pops a `long` from the stack.
     */
    fun popLong(): Long {
        return longs.pop()
    }

    /**
     * Pops a String from the stack.
     */
    fun popString(): String {
        return strings.pop()
    }

    /**
     * Pushes an `int` onto the stack.
     */
    fun pushInt(value: Int) {
        ints.push(value)
    }

    /**
     * Pushes a `long` onto the stack.
     */
    fun pushLong(value: Long) {
        longs.push(value)
    }

    /**
     * Pushes a String onto the stack.
     */
    fun pushString(value: String) {
        strings.push(value)
    }

    /**
     * Gets the amount of Strings in the stack.
     */
    fun stringCount(): Int {
        return strings.size
    }

    /**
     * Gets a section of the stack of Strings, as a [Stream].
     */
    internal fun getStrings(amount: Int): Stream<String> {
        return strings.stream().skip(amount.toLong())
    }

}
