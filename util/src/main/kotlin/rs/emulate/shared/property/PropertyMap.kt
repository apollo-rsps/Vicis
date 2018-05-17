package rs.emulate.shared.property

import java.util.Objects

/**
 * A [Map] wrapper containing methods to get properties using their opcode or name.
 *
 * @param properties The [Map] of [PropertyType]s to [Property] objects.
 * @param T The [type][PropertyType] of properties in this Map.
 */
class PropertyMap<T : PropertyType>(properties: Map<T, Property<*, T>>) {

    /**
     * The Map of PropertyTypes to Property objects.
     */
    private val properties: MutableMap<T, Property<*, T>> = properties.toMutableMap()

    /**
     * Validates that the specified property is valid (i.e. is not `null`).
     *
     * @param property The [Property] to validate.
     * @param key The key used to access the invalid property.
     * @return The property, if it is valid.
     * @throws IllegalArgumentException If the property is `null`.
     */
    private fun <V : Any, T : PropertyType> validate(property: Property<V, T>, key: Any): Property<V, T> {
        return Objects.requireNonNull(property, "No property with a key of $key exists.")
    }

    /**
     * Creates the PropertyMap, using an existing one as the base.
     */
    constructor(map: PropertyMap<T>) : this(map.properties)

    /**
     * Gets the [Property] with the specified [PropertyType].
     */
    operator fun <V : Any> get(name: PropertyType): Property<V, T> {
        val property = properties[name]!! as Property<V, T>

        return validate(property, name)
    }

    /**
     * Adds a [Property] to this PropertyMap.
     *
     * @param property The Property.
     */
    fun put(property: Property<*, T>) {
        properties[property.type] = property
    }

    /**
     * Gets the size of this PropertyMap.
     *
     * @return The size.
     */
    fun size(): Int {
        return properties.size
    }

    /**
     * Gets a [Collection] containing the values of this PropertyMap (i.e. the [Property] objects).
     *
     * @return The Collection of values.
     */
    fun values(): Collection<Property<*, T>> {
        return properties.values
    }

}
