package rs.emulate.legacy.config.`object`

import rs.emulate.legacy.config.ConfigConstants
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.alwaysFalse
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.asciiString
import rs.emulate.legacy.config.Properties.signedByte
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.`object`.ObjectProperty.*
import rs.emulate.legacy.config.npc.MorphismSet
import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * A default [ObjectDefinition] used as a base for actual definitions.
 *
 * @param <T> The type of ObjectDefinition this DefaultObjectDefinition is for.
 */
class DefaultObjectDefinition : DefaultConfigDefinition<ObjectDefinition>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val properties = HashMap<Int, SerializableProperty<*>>()

        properties[1] = SerializableProperty(POSITIONED_MODELS, ModelSet.EMPTY, ModelSet.Companion::encode,
            ModelSet.Companion::decodePositioned,
            { it.count * (java.lang.Short.BYTES + java.lang.Byte.BYTES) + java.lang.Byte.BYTES }
        )

        properties[2] = asciiString(NAME, "null")
        properties[3] = asciiString(DESCRIPTION, "null")

        properties[5] = SerializableProperty(MODELS, ModelSet.EMPTY, ModelSet.Companion::encode,
            ModelSet.Companion::decode, { java.lang.Byte.BYTES + it.count * java.lang.Short.BYTES }
        )

        properties[14] = unsignedByte(WIDTH, 1)
        properties[15] = unsignedByte(LENGTH, 1)
        properties[17] = alwaysFalse(SOLID, true)
        properties[18] = alwaysFalse(IMPENETRABLE, true)

        properties[19] = SerializableProperty(INTERACTIVE, false, DataBuffer::putBoolean, DataBuffer::getBoolean,
            java.lang.Byte.BYTES)

        properties[21] = alwaysTrue(CONTOUR_GROUND, false)
        properties[22] = alwaysTrue(DELAY_SHADING, false)
        properties[23] = alwaysTrue(OCCLUDE, false)
        properties[24] = unsignedShort(ANIMATION, -1) // TODO needs custom property, value should be -1 if 65,535

        properties[28] = unsignedByte(DECOR_DISPLACEMENT, 0)
        properties[29] = signedByte(AMBIENT_LIGHTING, 0)

        for (option in 1..ObjectDefinition.INTERACTION_COUNT) {
            val name = ConfigUtils.newOptionProperty(ObjectDefinition.INTERACTION_PROPERTY_PREFIX, option)
            properties[option + 29] = asciiString(name, "hidden")
        }

        properties[39] = signedByte(LIGHT_DIFFUSION, 0)
        properties[40] = ConfigUtils.newColourProperty(COLOURS)

        properties[60] = unsignedShort(MINIMAP_FUNCTION, -1)
        properties[62] = alwaysTrue(INVERTED, false)
        properties[64] = alwaysFalse(CAST_SHADOW, true)

        properties[65] = unsignedShort(SCALE_X, ConfigConstants.DEFAULT_SCALE)
        properties[66] = unsignedShort(SCALE_Y, ConfigConstants.DEFAULT_SCALE)
        properties[67] = unsignedShort(SCALE_Z, ConfigConstants.DEFAULT_SCALE)

        properties[68] = unsignedShort(MAPSCENE, -1)
        properties[69] = unsignedByte(SURROUNDINGS, 0)

        properties[70] = unsignedShort(TRANSLATION_X, 0)
        properties[71] = unsignedShort(TRANSLATION_Y, 0)
        properties[72] = unsignedShort(TRANSLATION_Z, 0)

        properties[73] = alwaysTrue(OBSTRUCTIVE_GROUND, false)
        properties[74] = alwaysTrue(HOLLOW, false)
        properties[75] = unsignedByte(SUPPORTS_ITEMS, 0)

        properties[77] = SerializableProperty(MORPHISM_SET, MorphismSet.EMPTY, MorphismSet.Companion::encode,
            MorphismSet.Companion::decode, { java.lang.Short.BYTES * (2 + it.count) + java.lang.Byte.BYTES }
        )

        return properties
    }

}
