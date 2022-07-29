package rs.emulate.legacy.widget.script

/**
 * A ClientScript from legacy client builds (widely known as CS1).
 *
 * Unlike CS2, legacy client scripts operate using a simple accumulator machine, with four registers:
 * - The register holding the program counter, which points to the next instruction to interpret.
 * - The operator register, containing the type of operator that should be evaluated by the next instruction.
 * - The value register, containing the current value of the ClientScript.
 * - The accumulator register, which contains the result of the evaluation of the current instruction, and will modify
 *   the current value stored in the value register using the operator type held in the operator register.
 *
 * @param instructions The [List] of [LegacyInstruction]s that make up this ClientScript.
 */
sealed class LegacyClientScript(instructions: List<LegacyInstruction>) {

    val instructions: List<LegacyInstruction> = instructions.toList()

    override fun toString(): String {
        return instructions.joinToString("\n", transform = LegacyInstruction::toString)
    }

    /**
     * A ClientScript that can have its result compared with a specified value, to yield `true` or `false`.
     *
     * @param operator The [RelationalOperator] used to test the result of the script.
     * @param comparate The value the execution result is compared against.
     */
    class Predicate(
        instructions: List<LegacyInstruction>,
        val operator: RelationalOperator,
        val comparate: Int
    ) : LegacyClientScript(instructions)

    /**
     * A ClientScript that only returns a numeric value.
     */
    class Mathematical(instructions: List<LegacyInstruction>) : LegacyClientScript(instructions)

}
