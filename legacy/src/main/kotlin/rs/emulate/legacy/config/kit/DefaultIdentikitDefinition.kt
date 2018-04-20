package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.COLOUR_COUNT
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.HEAD_MODEL_COUNT
import rs.emulate.legacy.config.kit.IdentikitDefinition.Companion.HEAD_MODEL_PREFIX
import rs.emulate.legacy.config.kit.IdentikitProperty.MODELS
import rs.emulate.legacy.config.kit.IdentikitProperty.PART
import rs.emulate.legacy.config.kit.IdentikitProperty.PLAYER_DESIGN_STYLE
import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * A default [IdentikitDefinition] used as a base for an actual definition.
 *
 * @param T The type of IdentikitDefinition this DefaultIdentikitDefinition is for.
 */
class DefaultIdentikitDefinition<T : IdentikitDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val defaults = HashMap<Int, SerializableProperty<*>>(27)

        defaults[1] = SerializableProperty(PART, Part.NULL, { buffer, part -> Part.encode(buffer, part) },
            Part.Companion::decode, java.lang.Byte.BYTES)

        val modelsEncoder = { buffer: DataBuffer, models: IntArray ->
            buffer.putByte(models.size)
            models.forEach { buffer.putShort(it) }
            buffer
        }

        defaults[2] = SerializableProperty(MODELS, null, modelsEncoder,
            ConfigUtils.MODEL_DECODER) { models -> models.size * java.lang.Short.SIZE + java.lang.Byte.SIZE }

        defaults[3] = alwaysTrue(PLAYER_DESIGN_STYLE, false)

        for (slot in 1..COLOUR_COUNT) {
            defaults[slot + 39] = unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot), 0)
            defaults[slot + 49] = unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot), 0)
        }

        for (model in 1..HEAD_MODEL_COUNT) {
            val name = ConfigUtils.newOptionProperty(HEAD_MODEL_PREFIX, model)
            defaults[model + 59] = unsignedShort(name, -1)
        }

        return defaults
    }

}
