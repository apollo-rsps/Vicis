package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for a Floor.
 */
open class FloorDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * The colour of the floor, in RGB.
     */
    val colour: SerializableProperty<Int>
        get() = getProperty(FloorProperty.Colour)

    /**
     * The colour when the floor is displayed on the minimap, in RGB.
     */
    val minimapColour: SerializableProperty<Int>
        get() = getProperty(FloorProperty.MinimapColour)

    val name: SerializableProperty<String>
        get() = getProperty(FloorProperty.Name)

    val shadowed: SerializableProperty<Boolean>
        get() = getProperty(FloorProperty.Occludes)

    val texture: SerializableProperty<Int>
        get() = getProperty(FloorProperty.Texture)

    companion object {
        const val ENTRY_NAME = "flo"
    }

}
