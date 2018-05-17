package rs.emulate.modern.script.decomp.instr.impl

import rs.emulate.modern.script.decomp.instr.Instruction
import rs.emulate.modern.script.decomp.instr.OperandType
import rs.emulate.modern.script.interp.ScriptContext

import java.util.function.Consumer

/**
 * A simple type of [Instruction] that does not take a single `int`/`long`/String operand.
 *
 * @param name The name of this [Instruction].
 * @param opcode The opcode of this Instruction.
 * @param action The [ScriptContext] [Consumer] that performs the side effect of this Instruction.
 * @param types The [OperandType]s of this Instruction.
 */
class GenericInstruction(
    name: String,
    opcode: Int,
    private val action: ScriptContext.() -> Unit,
    vararg types: OperandType
) : Instruction(name, opcode, *types) {

    override fun evaluate(context: ScriptContext) {
        context.action()
    }

}
