package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty

/**
 * A default [ParameterVariableDefinition] used as a base.
 */
class DefaultParameterVariableDefinition<T : ParameterVariableDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        return hashMapOf(
            5 to unsignedShort(ParameterVariableProperty.Parameter, 0)
        )
    }

}
