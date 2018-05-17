package rs.emulate.legacy.config

import com.google.common.base.MoreObjects
import kotlin.collections.Map.Entry

/**
 * A base class for a definition with properties that can be mutated.
 *
 * @param id The id of the MutableDefinition.
 * @param properties The [ConfigPropertyMap].
 */
abstract class MutableConfigDefinition(val id: Int, properties: ConfigPropertyMap) {

    protected val properties: ConfigPropertyMap = ConfigPropertyMap(properties)

    /**
     * Adds a [SerializableProperty] to this MutableDefinition, using the smallest available (positive) opcode.
     */
    fun addProperty(property: SerializableProperty<*>) {
        addProperty(properties.size(), property)
    }

    /**
     * Adds a [SerializableProperty] with the specified opcode to this MutableDefinition.
     */
    fun addProperty(opcode: Int, property: SerializableProperty<*>) {
        properties.put(opcode, property)
    }

    /**
     * Gets the [Set] of [Map] [Entry] objects containing the opcodes and [SerializableProperty] objects.
     */
    fun serializableProperties(): Set<Entry<Int, SerializableProperty<*>>> {
        return properties.serializableProperties()
    }

    /**
     * Gets a [SerializableProperty] with the specified opcode.
     */
    fun <T> getProperty(opcode: Int): SerializableProperty<T> {
        @Suppress("UNCHECKED_CAST")
        val property = properties.get<Any>(opcode) as SerializableProperty<T>
        return requireNotNull(property) { "No property with opcode $opcode exists." }
    }

    /**
     * Gets a [SerializableProperty] with the specified [ConfigPropertyType].
     */
    fun <T> getProperty(name: ConfigPropertyType<T>): SerializableProperty<T> {
        @Suppress("UNCHECKED_CAST")
        val property = properties[name]
        return requireNotNull(property) { "No property called $name exists." }
    }

    /**
     * Sets the [SerializableProperty] with the specified name.
     */
    fun setProperty(opcode: Int, property: SerializableProperty<*>) {
        properties.put(opcode, property)
    }

    /**
     * Sets the value of the [SerializableProperty] with the specified opcode.
     */
    fun <V> setProperty(name: ConfigPropertyType<V>, value: V) {
        getProperty(name).value = value
    }

    override fun toString(): String {
        val helper = MoreObjects.toStringHelper(this)

        for ((_, property) in properties.serializableProperties()) {
            if (property.valuePresent()) {
                val name = property.name

                helper.add(name, property)
            }
        }

        return helper.toString()
    }

}
