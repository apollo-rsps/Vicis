package rs.emulate.legacy.widget.script

import rs.emulate.util.cs.PlayerProvider

/**
 * An interpreter for [LegacyClientScript]s.
 *
 * @param script The [LegacyClientScript] to interpret.
 */
class LegacyInterpreter(
    private val script: LegacyClientScript,
    private val provider: PlayerProvider = PlayerProvider.defaultProvider()
) {

    /**
     * The operator that will be used to evaluate the next instruction.
     */
    private var operator: (Int, Int) -> Int = Int::plus

    /**
     * The current result of the execution.
     */
    var value: Int = 0

    /**
     * Interprets the [LegacyClientScript] stored in this interpreter.
     *
     * @throws IllegalStateException If the script does not end with a [LegacyInstructionType.RETURN] instruction.
     */
    fun interpret(): Int {
        for (instruction in script.instructions) {
            if (instruction.type == LegacyInstructionType.RETURN) {
                return value
            }

            val result = evaluate(instruction)
            if (result != null) {
                value = operator(value, result)
                operator = Int::plus
            }
        }

        throw IllegalArgumentException("Script must end with a return call.")
    }

    /**
     * Evaluates the specified [LegacyInstruction].
     */
    private fun evaluate(instruction: LegacyInstruction): Int? {
        val operands = instruction.operands

        return when (instruction.type) {
            LegacyInstructionType.RETURN -> throw IllegalStateException("Should never be called.")
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
                null
            }
            LegacyInstructionType.MULTIPLY -> {
                operator = Int::times
                null
            }
            LegacyInstructionType.DIVISION -> {
                operator = Int::div
                null
            }
            LegacyInstructionType.MOVE_ABSOLUTE_X -> provider.position.x
            LegacyInstructionType.MOVE_ABSOLUTE_Y -> provider.position.z
            LegacyInstructionType.MOVE -> operands[0]
        }
    }

}
