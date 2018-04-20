package rs.emulate.legacy.widget.script

/**
 * An interpreter for [LegacyClientScript]s.
 *
 * @param script The [LegacyClientScript] to interpret.
 * @param context The [ClientScriptContext] for the LegacyClientScript being interpreted.
 */
class LegacyInterpreter(private val script: LegacyClientScript, private val context: ClientScriptContext) {

    /**
     * The operator that will be used to evaluate the next instruction.
     */
    private var operator: (Int, Int) -> Int = Int::plus

    /**
     * Interprets the [LegacyClientScript] stored in this interpreter.
     *
     * @throws IllegalStateException If the script does not end with a [LegacyInstructionType.RETURN] instruction.
     */
    fun interpret(): Int {
        for (instruction in script.instructions) {
            val value = evaluate(instruction)
            if (context.finished) {
                return context.result
            }

            context.apply(operator, value)
        }

        throw IllegalArgumentException("Script must end with a return call.")
    }

    /**
     * Evaluates the specified [LegacyInstruction].
     */
    private fun evaluate(instruction: LegacyInstruction): Int {
        val operands = instruction.operands
        val provider = context.provider

        return when (instruction.type) {
            LegacyInstructionType.RETURN -> {
                context.finish()
                0 // TODO stop returning 0 here
            }
            LegacyInstructionType.MOVE_CURRENT_LEVEL -> provider.skill(operands[0]).currentLevel
            LegacyInstructionType.MOVE_MAX_LEVEL -> provider.skill(operands[0]).maximumLevel
            LegacyInstructionType.MOVE_EXPERIENCE -> provider.skill(operands[0]).experience.toInt()
            LegacyInstructionType.MOVE_ITEM_AMOUNT -> {
                TODO()
            }
            LegacyInstructionType.MOVE_SETTING -> provider.setting(operands[0])
            LegacyInstructionType.MOVE_EXPERIENCE_FOR_LEVEL -> {
                TODO()
            }
            LegacyInstructionType.MOVE_SETTING2 -> provider.setting(operands[0]) * 100 / 46875
            LegacyInstructionType.MOVE_COMBAT_LEVEL -> provider.combatLevel
            LegacyInstructionType.MOVE_TOTAL_LEVEL -> provider.totalLevel
            LegacyInstructionType.MOVE_CONTAINS_ITEM -> {
                TODO()
            }
            LegacyInstructionType.MOVE_RUN_ENERGY -> provider.runEnergy
            LegacyInstructionType.MOVE_WEIGHT -> provider.weight
            LegacyInstructionType.MOVE_SETTING_BIT -> {
                TODO()
            }
            LegacyInstructionType.MOVE_VARIABLE_BITS -> {
                TODO()
            }
            LegacyInstructionType.SUBTRACT -> {
                operator = Int::minus
                0
            }
            LegacyInstructionType.MULTIPLY -> {
                operator = Int::times
                0
            }
            LegacyInstructionType.DIVISION -> {
                operator = Int::div
                0
            }
            LegacyInstructionType.MOVE_ABSOLUTE_X -> provider.position.x
            LegacyInstructionType.MOVE_ABSOLUTE_Y -> provider.position.z
            LegacyInstructionType.MOVE -> operands[0]
        }
    }

}
