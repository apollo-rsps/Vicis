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
 * @param operator The [RelationalOperator] used to evaluate if the script state has changed.
 * @param default The default value of the clientscript.
 * @param instructions The [List] of [LegacyInstruction]s that make up this clientscript.
 */
class LegacyClientScript internal constructor(
    val operator: RelationalOperator,
    val default: Int,
    instructions: List<LegacyInstruction>
) {

    val instructions: List<LegacyInstruction> = instructions.toList()

    override fun toString(): String {
        return instructions.joinToString("\n", transform = LegacyInstruction::toString)
    }

}
