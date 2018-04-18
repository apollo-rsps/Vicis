package rs.emulate.legacy.config.graphic

import rs.emulate.legacy.config.ConfigConstants
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.graphic.GraphicDefinition.Companion.COLOUR_COUNT
import rs.emulate.legacy.config.graphic.GraphicProperty.*
import java.util.HashMap

/**
 * A default [GraphicDefinition] used as a base for an actual definition.
 *
 * @param T The type of GraphicDefinition this DefaultGraphicDefinition is for.
 */
class DefaultGraphicDefinition<T : GraphicDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val defaults = HashMap<Int, SerializableProperty<*>>(27)

        defaults[1] = unsignedShort(MODEL, 0)
        defaults[2] = unsignedShort(ANIMATION, -1)
        defaults[4] = unsignedShort(BREADTH_SCALE, ConfigConstants.DEFAULT_SCALE)
        defaults[5] = unsignedShort(DEPTH_SCALE, ConfigConstants.DEFAULT_SCALE)
        defaults[6] = unsignedShort(ROTATION, 0)
        defaults[7] = unsignedByte(BRIGHTNESS, 0)
        defaults[8] = unsignedByte(SHADOW, 0)

        for (slot in 1..COLOUR_COUNT) {
            defaults[slot + 40] = unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot), 0)
            defaults[slot + 50] = unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot), 0)
        }

        return defaults
    }

}
