package rs.emulate.modern.script.interp

import com.google.common.collect.ImmutableMap
import rs.emulate.modern.script.decomp.instr.Instruction
import rs.emulate.modern.script.decomp.instr.OperandType
import rs.emulate.modern.script.decomp.instr.impl.BranchInstruction
import rs.emulate.modern.script.decomp.instr.impl.GenericInstruction
import rs.emulate.modern.script.decomp.instr.impl.IntInstruction
import rs.emulate.modern.script.decomp.instr.impl.LongInstruction
import rs.emulate.modern.script.decomp.instr.impl.StringInstruction
import java.util.HashMap
import java.util.stream.Collectors

/**
 * A Map of opcodes to [Instruction]s.
 */
class InstructionMap {

    /**
     * The map of opcodes to functionality.
     */
    private val instructions: Map<Int, Instruction>

    /**
     * A creator for a [Map] of instruction opcodes to functions, that modify a [ScriptContext] using side-effects.
     */
    private class InstructionMapCreator {

        private val instructions = HashMap<Int, Instruction>(100)

        fun create(): Map<Int, Instruction> {
            return ImmutableMap.copyOf(instructions)
        }

        /**
         * Fills the instruction Map.
         */
        fun fill() {
            int("pushi", 0) { pushInt(intOperand) }
            string("pushs", 3) { pushString(stringOperand) }

            branch("goto", 6) { branch() }
            branch("ifi_neq", 7) { branchIf(popInt() != popInt()) }
            branch("ifi_eq", 8) { branchIf(popInt() == popInt()) }
            branch("ifi_lt", 9) { branchIf(popInt() > popInt()) }
            branch("ifi_gt", 10) { branchIf(popInt() < popInt()) }

            branch("ifi_geq", 31) { branchIf(popInt() <= popInt()) }
            branch("ifi_leq", 32) { branchIf(popInt() >= popInt()) }

            int("concat", 37) { getStrings(stringCount() - popInt()).collect(Collectors.joining()) }

            generic("popi", 38) { popInt() }
            generic("pops", 39) { popString() }

            int("pushl", 54) { pushLong(longOperand) }
            int("popl", 55) { popLong() }

            branch("ifl_neq", 68) { branchIf(popLong() != popLong()) }
            branch("ifl_eq", 69) { branchIf(popLong() == popLong()) }
            branch("ifl_lt", 70) { branchIf(popLong() > popLong()) }
            branch("ifl_gt", 71) { branchIf(popLong() < popLong()) }

            branch("ifl_geq", 72) { branchIf(popLong() <= popLong()) }
            branch("ifl_leq", 73) { branchIf(popLong() >= popLong()) }

            branch("if_true", 86) { branchIf(popInt() == 1) }
            branch("if_false", 87) { branchIf(popInt() == 0) }
        }

        private fun branch(name: String, opcode: Int, action: ScriptContext.() -> Unit) {
            insert(BranchInstruction(name, opcode, action))
        }

        private fun generic(name: String, opcode: Int, vararg types: OperandType, action: ScriptContext.() -> Unit) {
            insert(GenericInstruction(name, opcode, action, *types))
        }

        private fun int(name: String, opcode: Int, action: ScriptContext.() -> Unit) {
            insert(IntInstruction(name, opcode, action))
        }

        private fun long(name: String, opcode: Int, action: ScriptContext.() -> Unit) {
            insert(LongInstruction(name, opcode, action))
        }

        private fun string(name: String, opcode: Int, action: ScriptContext.() -> Unit) {
            insert(StringInstruction(name, opcode, action))
        }

        private fun insert(instruction: Instruction) {
            instructions[instruction.opcode] = instruction
        }

    }

    /**
     * Creates the InstructionMap.
     */
    init {
        val creator = InstructionMapCreator()
        creator.fill()
        instructions = creator.create()
    }

    /**
     * Executes the specified opcode for the specified [ScriptContext].
     */
    fun execute(opcode: Int, script: ScriptContext) {
        val function = instructions[opcode] ?: throw IllegalStateException("Unrecognised instruction $opcode.")
        function.evaluate(script)
    }

}
