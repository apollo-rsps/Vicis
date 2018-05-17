package rs.emulate.legacy.widget.script

/**
 * An operator used when interpreting a [LegacyClientScript].
 *
 * @param value The integer value.
 * @param token The String representation.
 */
internal enum class RelationalOperator constructor(val value: Int, val token: String) {

    NOT_EQUAL(1, "!="),
    GREATER_OR_EQUAL(2, ">="),
    LESS_OR_EQUAL(3, "<="),
    EQUAL(4, "=");


    companion object {

        /**
         * Gets the RelationalOperator with the specified integer value.
         * @throws IllegalArgumentException If no RelationalOperator with the specified integer value exists.
         */
        fun valueOf(value: Int): RelationalOperator {
            return values().find { operator -> operator.value == value }
                ?: throw IllegalArgumentException("No RelationalOperator with a value of $value exists.")
        }
    }

}
