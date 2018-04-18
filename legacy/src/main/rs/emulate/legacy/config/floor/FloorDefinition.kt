package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for a Floor.
 *
 * @param id The id of the definition.
 * @param properties The [ConfigPropertyMap].
 */
open class FloorDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the colour of this FloorDefinition, in rgb form.
     */
    fun colour(): SerializableProperty<Int> {
        return properties[FloorProperty.COLOUR]
    }

    /**
     * Gets the [SerializableProperty] containing the minimap colour of this FloorDefinition, in rgb form.
     */
    fun minimapColour(): SerializableProperty<Int> {
        return properties[FloorProperty.MINIMAP_COLOUR]
    }

    /**
     * Gets the [SerializableProperty] containing the name of this FloorDefinition.
     */
    fun name(): SerializableProperty<String> {
        return properties[FloorProperty.NAME]
    }

    /**
     * Gets the [SerializableProperty] containing the shadowed flag of this FloorDefinition.
     */
    fun shadowed(): SerializableProperty<Boolean> {
        return properties[FloorProperty.SHADOWED]
    }

    /**
     * Gets the [SerializableProperty] containing the texture id of this FloorDefinition.
     */
    fun texture(): SerializableProperty<Int> {
        return properties[FloorProperty.TEXTURE]
    }

}
