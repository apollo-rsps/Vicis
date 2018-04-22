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

        properties[1] = unsignedShort(MODEL, 0)
        properties[2] = asciiString(NAME, "null")
        properties[3] = asciiString(DESCRIPTION, "null")
        properties[4] = unsignedShort(SPRITE_SCALE, 2000)
        properties[5] = unsignedShort(SPRITE_PITCH, 0)
        properties[6] = unsignedShort(SPRITE_CAMERA_ROLL, 0)

        properties[7] = signedShort(SPRITE_TRANSLATE_X, 0)
        properties[8] = signedShort(SPRITE_TRANSLATE_Y, 0)

        properties[10] = unsignedShort(DynamicConfigPropertyType.valueOf("unknown", 10), 0)
        properties[11] = alwaysTrue(STACKABLE, false)
        properties[12] = unsignedInt(VALUE, 1)
        properties[16] = alwaysTrue(MEMBERS, false)

        properties[23] = SerializableProperty(PRIMARY_MALE_MODEL, PrimaryModel.EMPTY, PrimaryModel.Companion::encode,
            PrimaryModel.Companion::decode, java.lang.Short.BYTES + java.lang.Byte.BYTES
        )
        properties[24] = unsignedShort(SECONDARY_MALE_MODEL, 0)

        properties[25] = SerializableProperty(PRIMARY_FEMALE_MODEL, PrimaryModel.EMPTY, PrimaryModel.Companion::encode,
            PrimaryModel.Companion::decode, java.lang.Short.BYTES + java.lang.Byte.BYTES
        )
        properties[26] = unsignedShort(SECONDARY_FEMALE_MODEL, 0)

        for (action in 1..ItemConstants.MENU_ACTION_COUNT) {
            var type = ConfigUtils.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, action)
            properties[action + 29] = asciiString(type, "hidden")

            type = ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, action)
            properties[action + 34] = asciiString(type, "hidden")
        }

        properties[40] = ConfigUtils.newColourProperty(COLOURS)

        properties[78] = unsignedShort(TERTIARY_MALE_MODEL, 0)
        properties[79] = unsignedShort(TERTIARY_FEMALE_MODEL, 0)

        properties[90] = unsignedShort(PRIMARY_MALE_HEAD_PIECE, 0)
        properties[91] = unsignedShort(PRIMARY_FEMALE_HEAD_PIECE, 0)
        properties[92] = unsignedShort(SECONDARY_MALE_HEAD_PIECE, 0)
        properties[93] = unsignedShort(SECONDARY_FEMALE_HEAD_PIECE, 0)

        properties[95] = unsignedShort(SPRITE_CAMERA_YAW, 0)

        properties[97] = unsignedShort(NOTE_INFO_ID, 0)
        properties[98] = unsignedShort(NOTE_TEMPLATE_ID, 0)

        for (option in 1..ItemConstants.ITEM_STACK_COUNT) {
            val type = ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, option)

            properties[option + 99] = SerializableProperty(type, ItemStack.EMPTY,
                ItemStack.Companion::encode, ItemStack.Companion::decode, java.lang.Short.BYTES * 2
            )
        }

        properties[110] = unsignedShort(GROUND_SCALE_X, ConfigConstants.DEFAULT_SCALE)
        properties[111] = unsignedShort(GROUND_SCALE_Y, ConfigConstants.DEFAULT_SCALE)
        properties[112] = unsignedShort(GROUND_SCALE_Z, ConfigConstants.DEFAULT_SCALE)

        properties[113] = signedByte(AMBIENCE, 0)
        properties[114] = SerializableProperty(CONTRAST, 0, { buffer, contrast -> buffer.putByte(contrast / 5) },
            { it.getByte() * 5 }, java.lang.Byte.BYTES
        )

        properties[115] = unsignedByte(TEAM, 0)

        return properties
    }

}
