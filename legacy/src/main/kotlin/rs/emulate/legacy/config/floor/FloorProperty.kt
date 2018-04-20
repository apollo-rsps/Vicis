package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.ConfigPropertyType

/**
 * A [ConfigPropertyType] implementation for [FloorDefinition]s.
 *
 * @param opcode The opcode of the FloorProperty.
 */
enum class FloorProperty(override val opcode: Int) : ConfigPropertyType {

    /**
     * The colour FloorProperty.
     */
    COLOUR(1),

    /**
     * The texture id FloorProperty.
     */
    TEXTURE(2),

    /**
     * The shadowed FloorProperty.
     */
    SHADOWED(5),

    /**
     * The name FloorProperty.
     */
    NAME(6),

    /**
     * The minimap colour FloorProperty.
     */
    MINIMAP_COLOUR(7);

}
