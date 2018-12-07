package rs.emulate.legacy.config.varbit

/**
 * A definition for a bit variable ('varbit').
 */
class VarbitDefinition(
    val id: Int,
    var varp: Int = 0,
    var low: Int = 0,
    var high: Int = 0
) {

    companion object {

        val MASKS = IntArray(32) { index -> (Math.pow(2.0, index.toDouble()) - 1).toInt() }.also {
            it[31] = -1
        }

    }

}
