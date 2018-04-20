package rs.emulate.modern.script.decomp.instr

import rs.emulate.modern.script.interp.ScriptContext
import java.util.Arrays

/**
 * A ClientScript instruction.
 *
 * @param name The name of the Instruction.
 * @param opcode The opcode of the Instruction.
 * @param types The [OperandType]s.
 */
abstract class Instruction(val name: String, val opcode: Int, vararg types: OperandType) {

    /**
     * The Set of OperandTypes utilised by this Instruction.
     */
    protected val types: Set<OperandType> = types.toSet()

    /**
     * Gets the operand count of this Instruction.
     */
    val operandCount: Int
        get() = types.size

    /**
     * Gets the [OperandType] at the specified index.
     */
    fun containsType(type: OperandType): Boolean {
        return types.contains(type)
    }

    /**
     * Returns whether or not the specified [OperandType]s match the expected OperandTypes of this Instruction.
     */
    fun validOperandTypes(vararg types: OperandType): Boolean {
        return types == Arrays.asList(*types)
    }

    /**
     * Evaluates this Instruction.
     */
    abstract fun evaluate(context: ScriptContext)

}
