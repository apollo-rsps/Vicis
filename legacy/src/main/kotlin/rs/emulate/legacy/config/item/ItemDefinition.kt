package rs.emulate.legacy.config.item

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.shared.world.Sex

/**
 * A definition for an item (an 'obj' in the cache).
 *
 * @param id The id.
 * @param properties The [ConfigPropertyMap].
 */
open class ItemDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the [Map] of original to replacement colours.
     */
    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(ItemProperty.COLOURS)

    /**
     * Gets the [SerializableProperty] containing the description.
     */
    val description: SerializableProperty<String>
        get() = getProperty(ItemProperty.DESCRIPTION)

    /**
     * Gets the [SerializableProperty] containing the scale, in the x-direction, of this item's model when
     * displayed on the ground.
     */
    val groundScaleX: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_X)

    /**
     * Gets the [SerializableProperty] containing the scale, in the y-direction, of this item's model when
     * displayed on the ground.
     */
    val groundScaleY: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_Y)

    /**
     * Gets the [SerializableProperty] containing the scale, in the z-direction, of this item's model when
     * displayed on the ground.
     */
    val groundScaleZ: SerializableProperty<Int>
        get() = getProperty(ItemProperty.GROUND_SCALE_Z)

    /**
     * Gets the [SerializableProperty] containing the light ambiance.
     */
    val lightAmbiance: SerializableProperty<Int>
        get() = getProperty(ItemProperty.AMBIENCE)

    /**
     * Gets the [SerializableProperty] containing the light diffusion.
     */
    val lightDiffusion: SerializableProperty<Int>
        get() = getProperty(ItemProperty.CONTRAST)

    /**
     * Gets the [SerializableProperty] containing the model id.
     */
    val modelId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.MODEL)

    /**
     * Gets the [SerializableProperty] containing the name.
     */
    val name: SerializableProperty<String>
        get() = getProperty(ItemProperty.NAME)

    /**
     * Gets the [SerializableProperty] containing the note info id.
     */
    val noteInfoId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.NOTE_INFO_ID)

    /**
     * Gets the [SerializableProperty] containing the note template id.
     */
    val noteTemplateId: SerializableProperty<Int>
        get() = getProperty(ItemProperty.NOTE_TEMPLATE_ID)

    /**
     * Gets the [SerializableProperty] containing the sprite camera roll.
     */
    val spriteCameraRoll: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_CAMERA_ROLL)

    /**
     * Gets the [SerializableProperty] containing the sprite camera yaw.
     */
    val spriteCameraYaw: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_CAMERA_YAW)

    /**
     * Gets the [SerializableProperty] containing the sprite pitch.
     */
    val spritePitch: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_PITCH)

    /**
     * Gets the [SerializableProperty] containing the sprite scale.
     */
    val spriteScale: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_SCALE)

    /**
     * Gets the [SerializableProperty] containing the sprite translate x.
     */
    val spriteTranslateX: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_TRANSLATE_X)

    /**
     * Gets the [SerializableProperty] containing the sprite translate y.
     */
    val spriteTranslateY: SerializableProperty<Int>
        get() = getProperty(ItemProperty.SPRITE_TRANSLATE_Y)

    /**
     * Gets the [SerializableProperty] containing the team.
     */
    val team: SerializableProperty<Int>
        get() = getProperty(ItemProperty.TEAM)

    /**
     * Gets the [SerializableProperty] containing the value.
     */
    val value: SerializableProperty<Int>
        get() = getProperty(ItemProperty.VALUE)

    /**
     * Gets the [SerializableProperty] containing the members flag.
     */
    val isMembers: SerializableProperty<Boolean>
        get() = getProperty(ItemProperty.MEMBERS)

    /**
     * Gets the [SerializableProperty] containing the stackable flag.
     */
    val isStackable: SerializableProperty<Boolean>
        get() = getProperty(ItemProperty.STACKABLE)

    /**
     * Gets the [SerializableProperty] containing the ground action for the specified index.
     */
    fun getGroundAction(index: Int): SerializableProperty<String> {
        require(index in 0..ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, index))
    }

    /**
     * Gets the [SerializableProperty] containing the inventory action for the specified index.
     */
    fun getInventoryAction(index: Int): SerializableProperty<String> {
        require(index in 0..ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, index))
    }

    /**
     * Gets the [SerializableProperty] containing the item stack of the specified index.
     */
    fun getItemStack(index: Int): SerializableProperty<ItemStack> {
        require(index in 0..ItemConstants.ITEM_STACK_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, index))
    }

    /**
     * Gets the [SerializableProperty] containing the head model id for the specified [Sex].
     */
    fun getPrimaryHeadModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(
            if (sex.isMale) ItemProperty.PRIMARY_MALE_HEAD_PIECE else ItemProperty.PRIMARY_FEMALE_HEAD_PIECE)
    }

    /**
     * Gets the [SerializableProperty] containing the [PrimaryModel] for the specified [Sex].
     */
    fun getPrimaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) ItemProperty.PRIMARY_MALE_MODEL else ItemProperty.PRIMARY_FEMALE_MODEL)
    }

    /**
     * Gets the [SerializableProperty] containing the head model id for the specified [Sex].
     */
    fun getSecondaryHeadModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(
            if (sex.isMale) ItemProperty.SECONDARY_MALE_HEAD_PIECE else ItemProperty.SECONDARY_FEMALE_HEAD_PIECE)
    }

    /**
     * Gets the [SerializableProperty] containing the secondary model id for the specified [Sex].
     */
    fun getSecondaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) ItemProperty.SECONDARY_MALE_MODEL else ItemProperty.SECONDARY_FEMALE_MODEL)
    }

    /**
     * Gets the [SerializableProperty] containing the tertiary model id for the specified [Sex].
     */
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
