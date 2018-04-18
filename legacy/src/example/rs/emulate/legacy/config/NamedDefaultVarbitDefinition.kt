package rs.emulate.legacy.config

import rs.emulate.legacy.config.varbit.BitVariableDefinition
import rs.emulate.legacy.config.varbit.DefaultBitVariableDefinition

/**
 * A [DefaultBitVariableDefinition] that also includes a name.
 */
class NamedDefaultVarbitDefinition : DefaultBitVariableDefinition<BitVariableDefinition>() {

    @Override
    protected fun init(): Map<Integer, SerializableProperty<*>> {
        val properties = super.init()

        properties.put(2, Properties.asciiString(NAME_PROPERTY_TYPE, "null"))

        return properties
    }

    companion object {
        private val NAME_PROPERTY_TYPE = DynamicConfigPropertyType.valueOf("name", 2)
    }

}
