package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.COLOUR_COUNT
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.WIDGET_MODEL_COUNT
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.WIDGET_MODEL_PREFIX
import rs.emulate.legacy.config.kit.IdentikitProperty.Models
import rs.emulate.legacy.config.kit.IdentikitProperty.PlayerDesignStyle
import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * A default [IdentikitDefinition] used as a base for an actual definition.
 *
 * @param T The type of IdentikitDefinition this DefaultIdentikitDefinition is for.
 */
class DefaultIdentikitDefinition : DefaultConfigDefinition<IdentikitDefinition>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val defaults = HashMap<Int, SerializableProperty<*>>(27)

        defaults[1] = SerializableProperty(IdentikitProperty.Part, Part.NULL, Part.Companion::encode,
            Part.Companion::decode, java.lang.Byte.BYTES)

        val modelsEncoder = { buffer: DataBuffer, models: IntArray ->
            buffer.putByte(models.size)
            models.forEach { buffer.putShort(it) }
            buffer
        }

        defaults[2] = SerializableProperty(Models, IntArray(0), modelsEncoder, ConfigUtils.MODEL_DECODER) { models ->
            models.size * java.lang.Short.SIZE + java.lang.Byte.SIZE
        }

        defaults[3] = alwaysTrue(PlayerDesignStyle, false)

        for (slot in 0 until COLOUR_COUNT) {
            defaults[slot + 40] = unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot, 40), 0)
            defaults[slot + 50] = unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot, 50), 0)
        }

        for (model in 0 until WIDGET_MODEL_COUNT) {
            val name = ConfigUtils.newOptionProperty<Int>(WIDGET_MODEL_PREFIX, model, 60)
            defaults[model + 60] = unsignedShort(name, -1)
        }

        return defaults
    }

}
