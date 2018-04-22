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
        get() = properties[FloorProperty.COLOUR]

    /**
     * The colour when the floor is displayed on the minimap, in RGB.
     */
    val minimapColour: SerializableProperty<Int>
        get() = properties[FloorProperty.MINIMAP_COLOUR]

    val name: SerializableProperty<String>
        get() = properties[FloorProperty.NAME]

    val shadowed: SerializableProperty<Boolean>
        get() = properties[FloorProperty.SHADOWED]

    val texture: SerializableProperty<Int>
        get() = properties[FloorProperty.TEXTURE]

}
