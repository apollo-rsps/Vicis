package rs.emulate.legacy.config.graphic

import rs.emulate.legacy.config.ConfigPropertyType

/**
 * A [ConfigPropertyType] implementation for [GraphicDefinition]s.
 *
 * @param opcode The opcode.
 */
enum class GraphicProperty(override val opcode: Int) : ConfigPropertyType {

    /**
     * The model id property.
     */
    MODEL(1),

    /**
     * The animation id property.
     */
    ANIMATION(2),

    /**
     * The breadth scale property.
     */
    BREADTH_SCALE(4),

    /**
     * The depth scale property.
     */
    DEPTH_SCALE(5),

    /**
     * The rotation property.
     */
    ROTATION(6),

    /**
     * The brightness property.
     */
    BRIGHTNESS(7),

    /**
     * The shadow property.
     */
    SHADOW(8);

}
