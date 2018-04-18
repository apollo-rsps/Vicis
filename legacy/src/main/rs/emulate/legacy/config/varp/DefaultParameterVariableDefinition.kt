package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.varp.ParameterVariableProperty.PARAMETER

/**
 * A default [ParameterVariableDefinition] used as a base.
 *
 * @param T The type of ParameterVariableDefinition this DefaultParameterVariableDefinition is for.
 */
class DefaultParameterVariableDefinition<T : ParameterVariableDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        return hashMapOf(
            5 to unsignedShort(PARAMETER, 0)
        )
    }

}
