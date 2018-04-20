package rs.emulate.modern.script.decomp.instr

/**
 * The type of an operand for an instruction.
 *
 * @param clazz The Java [Class] representing this OperandType.
 */
enum class OperandType(val clazz: Class<*>) {

    /**
     * The integer operand type.
     */
    INT(Int::class.javaPrimitiveType!!),

    /**
     * The long operand type.
     */
    LONG(Long::class.javaPrimitiveType!!),

    /**
     * The String operand type.
     */
    STRING(String::class.java)

}
