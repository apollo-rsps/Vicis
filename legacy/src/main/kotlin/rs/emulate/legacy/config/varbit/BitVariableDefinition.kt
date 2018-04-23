package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for bit variables (a 'varbit').
 */
open class BitVariableDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the bit mask used by this variable.
     */
    val bitmask: Int
        get() = BIT_MASKS[high - low]

    /**
     * Gets the high bit mask index of this definition.
     */
    val high: Int
        get() = variableValue!!.high

    /**
     * Gets the low bit mask index of this definition.
     */
    val low: Int
        get() = variableValue!!.low

    /**
     * Gets the [SerializableProperty] containing the [Variable].
     */
    val variable: SerializableProperty<Variable>
        get() = getProperty(BitVariableProperty.Varbit)

    /**
     * Gets the variable id of this definition.
     */
    val variableId: Int
        get() = variableValue!!.variable

    /**
     * Gets the value of the [BitVariableProperty.VARIABLE] [SerializableProperty].
     */
    private val variableValue: Variable?
        get() = variable.value

    companion object {

        /**
         * The List of bit masks used by the BitVariables.
         */
        val BIT_MASKS: List<Int>

        /**
         * The name of the ArchiveEntry containing the BitVariableDefinitions, without the extension.
         */
        const val ENTRY_NAME = "varbit"

        init {
            val masks = IntArray(32) { index -> (Math.pow(2.0, index.toDouble()) - 1).toInt() }
            masks[31] = -1

            BIT_MASKS = masks.toList()
        }
    }

}
