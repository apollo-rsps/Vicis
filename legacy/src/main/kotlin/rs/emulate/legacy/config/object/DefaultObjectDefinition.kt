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

        properties[1] = SerializableProperty(PositionedModels, ModelSet.EMPTY, ModelSet.Companion::encode,
            ModelSet.Companion::decodePositioned,
            { it.count * (java.lang.Short.BYTES + java.lang.Byte.BYTES) + java.lang.Byte.BYTES }
        )

        properties[2] = asciiString(Name, "null")
        properties[3] = asciiString(Description, "null")

        properties[5] = SerializableProperty(Models, ModelSet.EMPTY, ModelSet.Companion::encode,
            ModelSet.Companion::decode, { java.lang.Byte.BYTES + it.count * java.lang.Short.BYTES }
        )

        properties[14] = unsignedByte(Width, 1)
        properties[15] = unsignedByte(Length, 1)
        properties[17] = alwaysFalse(Solid, true)
        properties[18] = alwaysFalse(Impenetrable, true)

        properties[19] = SerializableProperty(Interactive, false, DataBuffer::putBoolean, DataBuffer::getBoolean,
            java.lang.Byte.BYTES)

        properties[21] = alwaysTrue(ContourGround, false)
        properties[22] = alwaysTrue(DelayShading, false)
        properties[23] = alwaysTrue(Occlude, false)
        properties[24] = unsignedShort(Animation, -1) // TODO needs custom property, value should be -1 if 65,535

        properties[28] = unsignedByte(DecorDisplacement, 0)
        properties[29] = signedByte(AmbientLighting, 0)

        for (option in 0 until ObjectDefinition.INTERACTION_COUNT) {
            val name = ConfigUtils.newOptionProperty<String>(ObjectDefinition.INTERACTION_PROPERTY_PREFIX, option, 30)
            properties[option + 30] = asciiString(name, "hidden")
        }

        properties[39] = signedByte(LightDiffusion, 0)
        properties[40] = ConfigUtils.newColourProperty(Colours)

        properties[60] = unsignedShort(MinimapFunction, -1)
        properties[62] = alwaysTrue(Inverted, false)
        properties[64] = alwaysFalse(CastShadow, true)

        properties[65] = unsignedShort(ScaleX, ConfigConstants.DEFAULT_SCALE)
        properties[66] = unsignedShort(ScaleY, ConfigConstants.DEFAULT_SCALE)
        properties[67] = unsignedShort(ScaleZ, ConfigConstants.DEFAULT_SCALE)

        properties[68] = unsignedShort(Mapscene, -1)
        properties[69] = unsignedByte(Surroundings, 0)

        properties[70] = unsignedShort(TranslationX, 0)
        properties[71] = unsignedShort(TranslationY, 0)
        properties[72] = unsignedShort(TranslationZ, 0)

        properties[73] = alwaysTrue(ObstructiveGround, false)
        properties[74] = alwaysTrue(Hollow, false)
        properties[75] = unsignedByte(SupportsItems, 0)

        properties[77] = SerializableProperty(Morphisms, MorphismSet.EMPTY, MorphismSet.Companion::encode,
            MorphismSet.Companion::decode, { java.lang.Short.BYTES * (2 + it.count) + java.lang.Byte.BYTES }
        )

        return properties
    }

}
