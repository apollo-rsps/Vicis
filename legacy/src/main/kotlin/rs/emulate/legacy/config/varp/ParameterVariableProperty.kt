package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.ConfigPropertyType

/**
 * Contains [ConfigPropertyType] implementations for [ParameterVariableDefinition]s.
 *
 * @param opcode The opcode.
 */
internal enum class ParameterVariableProperty(override val opcode: Int) : ConfigPropertyType {

    /**
     * The parameter property.
     */
    PARAMETER(1);

}
