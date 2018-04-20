package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.ConfigPropertyType

/**
 * A [ConfigPropertyType] implementation for bit variables.
 *
 * @param opcode The opcode of the property.
 */
enum class BitVariableProperty(override val opcode: Int) : ConfigPropertyType {

    /**
     * The variable property.
     */
    VARIABLE(1);

}
