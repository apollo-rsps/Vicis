package rs.emulate.legacy.config

import rs.emulate.legacy.config.varbit.BitVariableDefinition

/**
 * A [BitVariableDefinition] with a name.
 *
 * @param id The id.
 * @param properties The [ConfigPropertyMap].
 */
class NamedVarbitDefinition(id: Int, properties: ConfigPropertyMap) : BitVariableDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the name of this BitVariableDefinition.
     */
    val name: SerializableProperty<String>
        get() = properties.get(NAME_PROPERTY_TYPE)

    companion object {
        private val NAME_PROPERTY_TYPE = DynamicConfigPropertyType.valueOf("name", 2)
    }

}
