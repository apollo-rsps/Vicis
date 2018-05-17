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

        properties[1] = SerializableProperty(Models, IntArray(0), modelEncoder, ConfigUtils.MODEL_DECODER) { models ->
            models.size * java.lang.Short.BYTES + java.lang.Byte.BYTES
        }

        properties[2] = asciiString(Name, "null")
        properties[3] = asciiString(Description, "null")
        properties[12] = unsignedByte(Size, 1)
        properties[13] = unsignedShort(IdleAnimation, -1)
        properties[14] = unsignedShort(WalkingAnimation, -1)

        properties[17] = SerializableProperty(AnimationSet, MovementAnimationSet.EMPTY,
            { buffer: DataBuffer, set: MovementAnimationSet -> MovementAnimationSet.encode(buffer, set) },
            { buffer: DataBuffer -> MovementAnimationSet.decode(buffer) }, java.lang.Short.BYTES * 4)

        for (option in 0 until NpcDefinition.INTERACTION_COUNT) {
            val name = ConfigUtils.newOptionProperty<String>(NpcDefinition.INTERACTION_PROPERTY_PREFIX, option, 30)
            properties[option + 30] = asciiString(name, "hidden")
        }

        properties[40] = ConfigUtils.newColourProperty(Colours)

        properties[60] = SerializableProperty(SecondaryModels, IntArray(0), modelEncoder, ConfigUtils.MODEL_DECODER)
        { models: IntArray -> models.size * java.lang.Short.BYTES + java.lang.Byte.BYTES }

        for (option in 0 until 3) {
            properties[option + 90] = Properties.unsignedShort(ConfigUtils.newOptionProperty("unused", option, 90), 0)
        }

        properties[93] = alwaysFalse(MinimapVisible, true)
        properties[95] = unsignedShort(CombatLevel, -1)
        properties[97] = unsignedShort(FlatScale, ConfigConstants.DEFAULT_SCALE)
        properties[98] = unsignedShort(HeightScale, ConfigConstants.DEFAULT_SCALE)
        properties[99] = alwaysTrue(PriorityRender, false)

        properties[100] = unsignedByte(LightModifier, 0)
        properties[101] = SerializableProperty(ShadowModifier, 0, { buffer, shadow -> buffer.putByte(shadow / 5) },
            { it.getByte() * 5 }, java.lang.Byte.BYTES
        )
        properties[102] = unsignedShort(HeadIcon, -1)
        properties[103] = unsignedShort(Rotation, 32)

        properties[106] = SerializableProperty(NpcProperty.Morphisms, MorphismSet.EMPTY,
            { buffer: DataBuffer, set: MorphismSet -> MorphismSet.encode(buffer, set) },
            { buffer: DataBuffer -> MorphismSet.decode(buffer) }
        ) { morphisms -> java.lang.Short.BYTES * (2 + morphisms.count) + java.lang.Byte.BYTES }

        properties[107] = alwaysFalse(Clickable, true)

        return properties
    }

}
