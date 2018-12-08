package rs.emulate.legacy.widget.script

/**
 * An instruction used in a [LegacyClientScript].
 *
 * @param value The integer value of the LegacyInstructionType.
 * @param mnemonic The mnemonic of the LegacyInstructionType.
 * @param operands The operand count of the LegacyInstructionType.
 */
enum class LegacyInstructionType(
    private val value: Int,
    val mnemonic: String,
    val operandCount: Int = OperandCount.ZERO
) { // TODO these doc comments suck

    /**
     * The return instruction, to signal the end of the script.
     */
    RETURN(0, "ret"),

    /**
     * Moves the current level of the skill with the id of the operand into the accumulator.
     *
     * Example: `mov_curr_lvl 0` moves the current level of the Attack skill into the accumulator.
     */
    MOVE_CURRENT_LEVEL(1, "mov_curr_lvl", OperandCount.ONE),

    /**
     * Moves the maximum level of the skill with the id of the operand into the accumulator.
     *
     * Example: `mov_max_lvl 0` moves the maximum level of the Attack skill into the accumulator.
     */
    MOVE_MAX_LEVEL(2, "mov_max_lvl", OperandCount.ONE),

    /**
     * Moves the experience of the skill with the id of the operand into the accumulator.
     *
     * Example: `mov_exp 0` moves the experience of the Attack skill into the accumulator.
     */
    MOVE_EXPERIENCE(3, "mov_exp", OperandCount.ONE),

    /**
     * Moves the amount of the item with the id of the second operand into the accumulator, looking in the inventory of
     * the widget with the id of the first operand.
     *
     * Example: `mov_item_amnt 10, 995` moves the amount of coins in the inventory of widget 10 into the
     * accumulator.
     */
    MOVE_ITEM_AMOUNT(4, "mov_item_amnt", OperandCount.TWO),

    /**
     * Moves the value of the setting with the id of the operand into the accumulator.
     *
     * Example: `mov_setting 0` moves the current value of setting 0 into the accumulator.
     */
    MOVE_SETTING(5, "mov_setting", OperandCount.ONE),

    /**
     * Moves the experience required to achieve the level specified by the first operand into the general purpose
     * register.
     *
     * Example `mov_exp_lvl 99` moves 13,034,431 into the accumulator.
     */
    MOVE_EXPERIENCE_FOR_LEVEL(6, "mov_exp_lvl", OperandCount.ONE),

    /**
     * Moves the value of the setting with the id of the operand into the accumulator.
     *
     * Example: `mov_setting 0` moves the current value of setting 0 into the accumulator.
     */
    MOVE_SETTING2(7, "mov_setting2", OperandCount.ONE), // TODO ! applies some sort of transformation

    /**
     * Moves the combat level of the local player into the accumulator.
     */
    MOVE_COMBAT_LEVEL(8, "mov_cmb_lvl"),

    /**
     * Moves the total level of the local player into the accumulator.
     */
    MOVE_TOTAL_LEVEL(9, "mov_ttl_lvl"),

    /**
     * Moves the value 999,999,999 (which instructs the client to display a too-large String, "*") into the general
     * purpose register, if the widget with the id specified by the first operand contains the item with the id
     * specified by the second operand.
     *
     * Example: `mov_max_amnt 10, 995` moves 999,999,999 into the accumulator if the inventory of widget 10
     * contains coins (item 995).
     */
    MOVE_CONTAINS_ITEM(10, "mov_max_amnt", OperandCount.TWO),

    /**
     * Moves the current run energy level of the local player into the accumulator.
     */
    MOVE_RUN_ENERGY(11, "mov_run"),

    /**
     * Moves the current weight level of the local player into the accumulator.
     */
    MOVE_WEIGHT(12, "mov_weight"),

    /**
     * Moves the boolean result (i.e. either 1 or 0) of the setting specified by the first operand into the general
     * purpose register. The second operand specifies the bit that should be checked for truth
     */
    MOVE_SETTING_BIT(13, "mov_setting_bit", OperandCount.TWO),

    /**
     * Moves the value of the bit from the variable bits with the id specified by the first operand into the general
     * purpose register.
     */
    MOVE_VARIABLE_BITS(14, "mov_varbit", OperandCount.ONE),

    /**
     * Specifies that the next operation that should be performed is a subtraction.
     */
    SUBTRACT(15, "sub"),

    /**
     * Specifies that the next operation that should be performed is a division.
     */
    DIVISION(16, "div"),

    /**
     * Specifies that the next operation that should be performed is a multiplication.
     */
    MULTIPLY(17, "mult"),

    /**
     * Moves the absolute x coordinate of the local player into the accumulator.
     */
    MOVE_ABSOLUTE_X(18, "mov_abs_x"),

    /**
     * Moves the absolute y coordinate of the local player into the accumulator.
     */
    MOVE_ABSOLUTE_Y(19, "mov_abs_y"),

    /**
     * Moves the value specified by the first operand into the accumulator.
     *
     * Example: `mov 42` moves 42 into the accumulator.
     */
    MOVE(20, "mov", OperandCount.ONE);

    /**
     * Gets the integer value of this LegacyInstructionType.
     */
    fun toInteger(): Short {
        return value.toShort()
    }

    companion object {

        /**
         * Gets the LegacyInstructionType with the specified mnemonic.
         *
         * @throws IllegalArgumentException If no LegacyInstructionType with the specified mnemonic value exists.
         */
        fun forMnemonic(mnemonic: String): LegacyInstructionType {
            return values().find { instruction -> mnemonic == instruction.mnemonic }
                ?: throw IllegalArgumentException("No instruction with the specified integer value exists.")
        }

        /**
         * Gets the LegacyInstructionType with the specified integer value.
         *
         * @throws IllegalArgumentException If no LegacyInstructionType with the specified integer value exists.
         */
        fun valueOf(value: Int): LegacyInstructionType {
            return values().find { instruction -> instruction.value == value }
                ?: throw IllegalArgumentException("No instruction with the specified integer value exists.")
        }
    }

}
