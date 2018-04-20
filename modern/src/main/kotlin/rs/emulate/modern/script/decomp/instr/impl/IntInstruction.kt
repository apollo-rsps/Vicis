package rs.emulate.modern.script.decomp.instr.impl

import rs.emulate.modern.script.decomp.instr.Instruction
import rs.emulate.modern.script.decomp.instr.OperandType
import rs.emulate.modern.script.interp.ScriptContext

import java.util.function.Consumer

/**
 * An [Instruction] that takes only a single `int` operand.
 *
 * @param name The name of this IntInstruction.
 * @param opcode The opcode of this IntInstruction.
 * @param action The [ScriptContext] [Consumer] that performs the side effect of this IntInstruction.
 */
class IntInstruction(
    name: String,
    opcode: Int,
    private val action: ScriptContext.() -> Unit
) : Instruction(name, opcode, OperandType.INT) {

    override fun evaluate(context: ScriptContext) {
        context.action()
    }

}
