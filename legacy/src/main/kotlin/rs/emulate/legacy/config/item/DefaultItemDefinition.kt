package rs.emulate.legacy.config.item

import rs.emulate.legacy.config.ConfigConstants
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.DynamicConfigPropertyType
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.asciiString
import rs.emulate.legacy.config.Properties.signedByte
import rs.emulate.legacy.config.Properties.signedShort
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedInt
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.item.ItemProperty.*
import java.util.HashMap

/**
 * A default [ItemDefinition] used as a base for actual definitions.
 */
class DefaultItemDefinition : DefaultConfigDefinition<ItemDefinition>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val properties = HashMap<Int, SerializableProperty<*>>()

        properties[1] = unsignedShort(Model, 0)
        properties[2] = asciiString(Name, "null")
        properties[3] = asciiString(Description, "null")
        properties[4] = unsignedShort(SpriteScale, 2000)
        properties[5] = unsignedShort(SpritePitch, 0)
        properties[6] = unsignedShort(SpriteCameraRoll, 0)

        properties[7] = signedShort(SpriteTranslateX, 0)
        properties[8] = signedShort(SpriteTranslateY, 0)

        properties[10] = unsignedShort(DynamicConfigPropertyType.valueOf("unknown", 10), 0)
        properties[11] = alwaysTrue(Stackable, false)
        properties[12] = unsignedInt(Value, 1)
        properties[16] = alwaysTrue(Members, false)

        properties[23] = SerializableProperty(PrimaryMaleModel, PrimaryModel.EMPTY, PrimaryModel.Companion::encode,
            PrimaryModel.Companion::decode, java.lang.Short.BYTES + java.lang.Byte.BYTES
        )
        properties[24] = unsignedShort(SecondaryMaleModel, 0)

        properties[25] = SerializableProperty(PrimaryFemaleModel, PrimaryModel.EMPTY, PrimaryModel.Companion::encode,
            PrimaryModel.Companion::decode, java.lang.Short.BYTES + java.lang.Byte.BYTES
        )
        properties[26] = unsignedShort(SecondaryFemaleModel, 0)

        for (action in 1..ItemConstants.MENU_ACTION_COUNT) {
            var type = ConfigUtils.newOptionProperty<String>(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, action)
            properties[action + 29] = asciiString(type, "hidden")

            type = ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, action)
            properties[action + 34] = asciiString(type, "hidden")
        }

        properties[40] = ConfigUtils.newColourProperty(Colours)

        properties[78] = unsignedShort(TertiaryMaleModel, 0)
        properties[79] = unsignedShort(TertiaryFemaleModel, 0)

        properties[90] = unsignedShort(PrimaryMaleHeadPiece, 0)
        properties[91] = unsignedShort(PrimaryFemaleHeadPiece, 0)
        properties[92] = unsignedShort(SecondaryMaleHeadPiece, 0)
        properties[93] = unsignedShort(SecondaryFemaleHeadPiece, 0)

        properties[95] = unsignedShort(SpriteCameraYaw, 0)

        properties[97] = unsignedShort(NoteInfoId, 0)
        properties[98] = unsignedShort(NoteTemplateId, 0)

        for (option in 1..ItemConstants.ITEM_STACK_COUNT) {
            val type = ConfigUtils.newOptionProperty<ItemStack>(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, option)

            properties[option + 99] = SerializableProperty(type, ItemStack.EMPTY,
                ItemStack.Companion::encode, ItemStack.Companion::decode, java.lang.Short.BYTES * 2
            )
        }

        properties[110] = unsignedShort(GroundScaleX, ConfigConstants.DEFAULT_SCALE)
        properties[111] = unsignedShort(GroundScaleY, ConfigConstants.DEFAULT_SCALE)
        properties[112] = unsignedShort(GroundScaleZ, ConfigConstants.DEFAULT_SCALE)

        properties[113] = signedByte(Ambience, 0)
        properties[114] = SerializableProperty(Contrast, 0, { buffer, contrast -> buffer.putByte(contrast / 5) },
            { it.getByte() * 5 }, java.lang.Byte.BYTES
        )

        properties[115] = unsignedByte(Team, 0)

        return properties
    }

}
