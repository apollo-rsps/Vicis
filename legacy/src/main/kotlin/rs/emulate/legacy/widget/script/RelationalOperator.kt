package rs.emulate.legacy.widget.script

/**
 * An operator used when interpreting a [LegacyClientScript].
 *
 * @param value The integer value.
 * @param token The String representation.
 */
enum class RelationalOperator constructor(val value: Int, val token: String) {

    EQUAL(1, "="),
    LESS_THAN(2, "<"),
    GREATER_THAN(3, ">"),
    NOT_EQUAL(4, "!=");

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
