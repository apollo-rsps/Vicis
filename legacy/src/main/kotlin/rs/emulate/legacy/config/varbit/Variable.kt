package rs.emulate.legacy.config.varbit

/**
 * A variable used in a definition, with a variable id, a high bit mask index, and a low bit mask index.
 */
class Variable private constructor(
    val variable: Int,
    val low: Int,
    val high: Int
) {

    /**
     * Creates an empty Variable with `-1` for each value.
     */
    private constructor() : this(-1, -1, -1)

    companion object {

        fun create(variable: Int, high: Int, low: Int): Variable {
            require(variable >= 0) { "Varbit id must be greater than or equal to 0." }
            require(validMaskIndex(high) && validMaskIndex(low)) {
                "High and low mask indices must be greater than or equal to 0 and less than 33."
            }
            require(high >= low) { "High bit mask must be greater than or equal to the low bit mask." }

            return Variable(variable, low, high)
        }

        /**
         * The empty Variable used as the default value.
         */
        val EMPTY = Variable()

        /**
         * Returns whether or not the specified index is valid for a mask.
         */
        fun validMaskIndex(index: Int): Boolean {
            return index in 0..32
        }
    }

}
