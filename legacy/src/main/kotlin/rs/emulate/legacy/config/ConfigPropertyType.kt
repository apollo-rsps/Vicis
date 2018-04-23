package rs.emulate.legacy.config

import rs.emulate.shared.property.PropertyType

/**
 * A [PropertyType] used by the [ConfigEncoder] and [ConfigDecoder].
 */
abstract class ConfigPropertyType<T> : PropertyType {

    /**
     * The opcode of this ConfigPropertyType.
     * */
    abstract val opcode: Int

    override val name = javaClass.simpleName!!

    /**
     * Gets the [SerializableProperty] associated with this ConfigPropertyType from the specified
     * [MutableConfigDefinition].
     */
    fun propertyFrom(definition: MutableConfigDefinition): SerializableProperty<T> {
        return definition.getProperty(this)
    }

    override fun formattedName(): String {
        return javaClass.simpleName!!.replace("(.)([A-Z])".toRegex(), "$1 $2")
    }

}
