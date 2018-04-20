package rs.emulate.modern.script.decomp.instr

/**
 * A table of operands of a script.
 *
 * @param ints The `int` operands.
 * @param longs The `long` operands.
 * @param strings The String operands.
 */
class OperandTable(
    private val ints: IntArray? = null,
    private val longs: LongArray? = null,
    private val strings: Array<String>? = null
) {

    /**
     * Gets an `int` operand at the specified index.
     */
    fun getInt(index: Int): Int {
        requireNotNull(ints) { "Cannot get an int operand from a script with no integer operands." }
        require(index < ints!!.size) { "Integer operand index out of bounds." }

        return ints[index]
    }

    /**
     * Gets a `long` operand at the specified index.
     */
    fun getLong(index: Int): Long {
        requireNotNull(longs) { "Cannot get an int operand from a script with no integer operands." }
        require(index < longs!!.size) { "Integer operand index out of bounds." }

        return longs[index]
    }

    /**
     * Gets a String operand at the specified index.
     */
    fun getString(index: Int): String {
        requireNotNull(strings) { "Cannot get an int operand from a script with no integer operands." }
        require(index < strings!!.size) { "Integer operand index out of bounds." }

        return strings[index]
    }

}
