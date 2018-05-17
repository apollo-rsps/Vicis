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
 */
class DefaultGraphicDefinition : DefaultConfigDefinition<GraphicDefinition>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val defaults = HashMap<Int, SerializableProperty<*>>(27)

        defaults[1] = unsignedShort(Model, 0)
        defaults[2] = unsignedShort(Animation, -1)
        defaults[4] = unsignedShort(BreadthScale, ConfigConstants.DEFAULT_SCALE)
        defaults[5] = unsignedShort(DepthScale, ConfigConstants.DEFAULT_SCALE)
        defaults[6] = unsignedShort(Rotation, 0)
        defaults[7] = unsignedByte(Brightness, 0)
        defaults[8] = unsignedByte(Shadow, 0)

        for (slot in 0 until COLOUR_COUNT) {
            defaults[slot + 40] = unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot, 40), 0)
            defaults[slot + 50] = unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot, 50), 0)
        }

        return defaults
    }

}
