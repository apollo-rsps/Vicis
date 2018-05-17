package rs.emulate.modern.script.decomp.instr.impl

import rs.emulate.modern.script.decomp.instr.Instruction
import rs.emulate.modern.script.decomp.instr.OperandType
import rs.emulate.modern.script.interp.ScriptContext

import java.util.function.Consumer

/**
 * An [Instruction] that takes only a single `long` operand.
 *
 * @param name The name of this LongInstruction.
 * @param opcode The opcode of this LongInstruction.
 * @param action The [ScriptContext] [Consumer] that performs the side effect of this LongInstruction.
 */
class LongInstruction(
    name: String,
    opcode: Int,
    private val action: (ScriptContext) -> Unit
) : Instruction(name, opcode, OperandType.STRING) {

    override fun evaluate(context: ScriptContext) {
        action(context)
    }

}
