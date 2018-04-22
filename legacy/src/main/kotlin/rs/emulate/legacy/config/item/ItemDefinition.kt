package rs.emulate.legacy.config.item

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.shared.world.Sex

/**
 * A definition for an item (an 'obj' in the cache).
 */
open class ItemDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * The [Map] of original to replacement colours.
     */
    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(ItemProperty.COLOURS)

    val description: SerializableProperty<String>
        get() = getProperty(ItemProperty.DESCRIPTION)

    /**
     * The scale, in the x-direction, of this item's model when displayed on the ground.
     */
    val groundScaleX: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_X)

    /**
     * The scale, in the vertical direction, of this item's model when displayed on the ground.
     */
    val groundScaleY: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_Y)

    /**
     * The scale, in the z-direction, of this item's model when displayed on the ground.
     */
    val groundScaleZ: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_Z)

    val lightAmbiance: SerializableProperty<Int>
        get() = getProperty(ItemProperty.AMBIENCE)

    val lightDiffusion: SerializableProperty<Int>
        get() = getProperty(ItemProperty.CONTRAST)

    val modelId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.MODEL)

    val name: SerializableProperty<String>
        get() = getProperty(ItemProperty.NAME)

    val noteInfoId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.NOTE_INFO_ID)

    val noteTemplateId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.NOTE_TEMPLATE_ID)

    val spriteCameraRoll: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_CAMERA_ROLL)

    val spriteCameraYaw: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_CAMERA_YAW)

    val spritePitch: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_PITCH)

    val spriteScale: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_SCALE)

    val spriteTranslateX: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_TRANSLATE_X)

    val spriteTranslateY: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_TRANSLATE_Y)

    val team: SerializableProperty<Int>
        get() = getProperty(ItemProperty.TEAM)

    val value: SerializableProperty<Int>
        get() = getProperty(ItemProperty.VALUE)

    val isMembers: SerializableProperty<Boolean>
        get() = getProperty(ItemProperty.MEMBERS)

    val isStackable: SerializableProperty<Boolean>
        get() = getProperty(ItemProperty.STACKABLE)

    fun getGroundAction(index: Int): SerializableProperty<String> {
        require(index in 0 until ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, 30 + index))
    }

    fun getInventoryAction(index: Int): SerializableProperty<String> {
        require(index in 0 until ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, 35 + index))
    }

    fun getItemStack(index: Int): SerializableProperty<ItemStack> {
        require(index in 0 until ItemConstants.ITEM_STACK_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, 100 + index))
    }

    fun getPrimaryHeadModel(sex: Sex): SerializableProperty<Int> {
        val property = if (sex.isMale) ItemProperty.PRIMARY_MALE_HEAD_PIECE else ItemProperty.PRIMARY_FEMALE_HEAD_PIECE
        return getProperty(property)
    }

    fun getPrimaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) ItemProperty.PRIMARY_MALE_MODEL else ItemProperty.PRIMARY_FEMALE_MODEL)
    }

    fun getSecondaryHeadModel(sex: Sex): SerializableProperty<Int> {
        val property = if (sex.isMale) ItemProperty.SECONDARY_MALE_HEAD_PIECE else ItemProperty.SECONDARY_FEMALE_HEAD_PIECE
        return getProperty(property)
    }

    fun getSecondaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) ItemProperty.SECONDARY_MALE_MODEL else ItemProperty.SECONDARY_FEMALE_MODEL)
    }

    fun getTertiaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) ItemProperty.TERTIARY_MALE_MODEL else ItemProperty.TERTIARY_FEMALE_MODEL)
    }

    companion object {

        /**
         * The name of the archive entry containing the item definitions, without the extension.
         */
        const val ENTRY_NAME = "obj"

    }

}
