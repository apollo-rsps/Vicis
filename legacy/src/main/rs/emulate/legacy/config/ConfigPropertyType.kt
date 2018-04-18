package rs.emulate.legacy.config

import rs.emulate.shared.property.PropertyType

/**
 * A [PropertyType] used as part of the [ConfigEncoder] and [ConfigDecoder], [opcode].
 * Should **only** be implemented by enumerators (excluding the existing [DynamicConfigPropertyType] class).
 */
interface ConfigPropertyType : PropertyType {

    /**
     * Gets the [SerializableProperty] associated with this ConfigPropertyType from the specified
     * [MutableConfigDefinition].
     */
    fun <T : Any> propertyFrom(definition: MutableConfigDefinition): SerializableProperty<T> {
        return definition.getProperty(this)
    }

    /**
     * The opcode of this ConfigPropertyType.
     * */
    val opcode: Int

}
