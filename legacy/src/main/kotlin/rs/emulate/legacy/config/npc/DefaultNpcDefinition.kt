package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigConstants
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties
import rs.emulate.legacy.config.Properties.alwaysFalse
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.asciiString
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.npc.NpcProperty.*
import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * A default [NpcDefinition] used as a base.
 */
class DefaultNpcDefinition : DefaultConfigDefinition<NpcDefinition>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val properties = HashMap<Int, SerializableProperty<*>>()

        val modelEncoder: (DataBuffer, IntArray?) -> DataBuffer = { buffer, models ->
            buffer.putByte(models!!.size)
            models.forEach { buffer.putShort(it) }
            buffer
        }

        properties[1] = SerializableProperty(MODELS, null, modelEncoder,
            ConfigUtils.MODEL_DECODER) { models: IntArray ->
            models.size * java.lang.Short.BYTES + java.lang.Byte.BYTES
        }

        properties[2] = asciiString(NAME, "null")
        properties[3] = asciiString(DESCRIPTION, "null")
        properties[12] = unsignedByte(SIZE, 1)
        properties[13] = unsignedShort(IDLE_ANIMATION, -1)
        properties[14] = unsignedShort(WALKING_ANIMATION, -1)

        properties[17] = SerializableProperty(ANIMATION_SET, MovementAnimationSet.EMPTY,
            { buffer: DataBuffer, set: MovementAnimationSet -> MovementAnimationSet.encode(buffer, set) },
            { buffer: DataBuffer -> MovementAnimationSet.decode(buffer) }, java.lang.Short.BYTES * 4)

        for (option in 1..NpcDefinition.INTERACTION_COUNT) {
            val name = ConfigUtils.newOptionProperty(NpcDefinition.INTERACTION_PROPERTY_PREFIX, option)
            properties[option + 29] = asciiString(name, "hidden")
        }

        properties[40] = ConfigUtils.newColourProperty(COLOURS)

        properties[60] = SerializableProperty(SECONDARY_MODELS, null, modelEncoder, ConfigUtils.MODEL_DECODER)
        { models: IntArray -> models.size * java.lang.Short.BYTES + java.lang.Byte.BYTES }

        for (option in 1..3) {
            properties[option + 89] = Properties.unsignedShort(ConfigUtils.newOptionProperty("unused", option), 0)
        }

        properties[93] = alwaysFalse(MINIMAP_VISIBLE, true)
        properties[95] = unsignedShort(COMBAT_LEVEL, -1)
        properties[97] = unsignedShort(FLAT_SCALE, ConfigConstants.DEFAULT_SCALE)
        properties[98] = unsignedShort(HEIGHT_SCALE, ConfigConstants.DEFAULT_SCALE)
        properties[99] = alwaysTrue(PRIORITY_RENDER, false)

        properties[100] = unsignedByte(LIGHT_MODIFIER, 0)
        properties[101] = unsignedByte(SHADOW_MODIFIER, 0) // TODO !! needs to multiply by 5
        properties[102] = unsignedShort(HEAD_ICON, -1)
        properties[103] = unsignedShort(ROTATION, 32)

        properties[106] = SerializableProperty(MORPHISM_SET, MorphismSet.EMPTY,
            { buffer: DataBuffer, set: MorphismSet -> MorphismSet.encode(buffer, set) },
            { buffer: DataBuffer -> MorphismSet.decode(buffer) }
        ) { morphisms -> java.lang.Short.BYTES * (2 + morphisms.count) + java.lang.Byte.BYTES }

        properties[107] = alwaysFalse(CLICKABLE, true)

        return properties
    }

}
