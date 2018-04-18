package rs.emulate.legacy.widget.script

/**
 * An instruction used as part of a [LegacyClientScript].
 *
 * @param type The [LegacyInstructionType].
 * @param operands The operands of the LegacyInstruction.
 */
class LegacyInstruction private constructor(val type: LegacyInstructionType, operands: IntArray) {

    val operands: IntArray = operands.clone()
        get() = field.clone()

    override fun toString(): String {
        return type.mnemonic + operands.joinToString(transform = Int::toString)
    }

    companion object {

        /**
         * Creates a LegacyInstruction.
         *
         * @throws IllegalArgumentException If `operands.length` is not equal to the type's operand count.
         */
        fun create(type: LegacyInstructionType, operands: IntArray): LegacyInstruction {
            require(operands.size == type.operandCount) {
                "Operands length must be equal to the InstructionType operand count."
            }

            return LegacyInstruction(type, operands)
        }
    }

}
