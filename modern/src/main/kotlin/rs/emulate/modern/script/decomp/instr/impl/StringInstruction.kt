package rs.emulate.modern.script.decomp.instr.impl

import rs.emulate.modern.script.decomp.instr.Instruction
import rs.emulate.modern.script.decomp.instr.OperandType
import rs.emulate.modern.script.interp.ScriptContext

import java.util.function.Consumer

/**
 * An [Instruction] that takes only a single String operand.
 *
 * @param name The name of this StringInstruction.
 * @param opcode The opcode of this StringInstruction.
 * @param action The [ScriptContext] [Consumer] that performs the side effect of this StringInstruction.
 */
class StringInstruction(
    name: String,
    opcode: Int,
    private val action: ScriptContext.() -> Unit
) : Instruction(name, opcode, OperandType.STRING) {

    override fun evaluate(context: ScriptContext) {
        context.action()
    }

}
