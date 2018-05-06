package rs.emulate.legacy.config.item

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.item.ItemProperty.*
import rs.emulate.shared.world.Sex

/**
 * A definition for an item (an 'obj' in the cache).
 */
open class ItemDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * The [Map] of original to replacement colours.
     */
    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(Colours)

    val description: SerializableProperty<String>
        get() = getProperty(Description)

    /**
     * The scale, in the x-direction, of this item's model when displayed on the ground.
     */
    val groundScaleX: SerializableProperty<Int>
        get() = getProperty(GroundScaleX)

    /**
     * The scale, in the vertical direction, of this item's model when displayed on the ground.
     */
    val groundScaleY: SerializableProperty<Int>
        get() = getProperty(GroundScaleY)

    /**
     * The scale, in the z-direction, of this item's model when displayed on the ground.
     */
    val groundScaleZ: SerializableProperty<Int>
        get() = getProperty(GroundScaleZ)

    val lightAmbiance: SerializableProperty<Int>
        get() = getProperty(Ambience)

    val lightDiffusion: SerializableProperty<Int>
        get() = getProperty(Contrast)

    val modelId: SerializableProperty<Int>
        get() = getProperty(Model)

    val name: SerializableProperty<String>
        get() = getProperty(Name)

    val noteInfoId: SerializableProperty<Int>
        get() = getProperty(NoteInfoId)

    val noteTemplateId: SerializableProperty<Int>
        get() = getProperty(NoteTemplateId)

    val spriteCameraRoll: SerializableProperty<Int>
        get() = getProperty(SpriteCameraRoll)

    val spriteCameraYaw: SerializableProperty<Int>
        get() = getProperty(SpriteCameraYaw)

    val spritePitch: SerializableProperty<Int>
        get() = getProperty(SpritePitch)

    val spriteScale: SerializableProperty<Int>
        get() = getProperty(SpriteScale)

    val spriteTranslateX: SerializableProperty<Int>
        get() = getProperty(SpriteTranslateX)

    val spriteTranslateY: SerializableProperty<Int>
        get() = getProperty(SpriteTranslateY)

    val team: SerializableProperty<Int>
        get() = getProperty(Team)

    val value: SerializableProperty<Long>
        get() = getProperty(Value)

    val isMembers: SerializableProperty<Boolean>
        get() = getProperty(Members)

    val isStackable: SerializableProperty<Boolean>
        get() = getProperty(Stackable)

    fun getGroundAction(index: Int): SerializableProperty<String> {
        require(index in 0 until ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, index, 30))
    }

    fun getInventoryAction(index: Int): SerializableProperty<String> {
        require(index in 0 until ItemConstants.MENU_ACTION_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, index, 35))
    }

    fun getItemStack(index: Int): SerializableProperty<ItemStack> {
        require(index in 0 until ItemConstants.ITEM_STACK_COUNT) {
            "Index must be non-negative and less than or equal to $index."
        }
        return getProperty(ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, index, 100))
    }

    fun getPrimaryHeadModel(sex: Sex): SerializableProperty<Int> {
        val property = if (sex.isMale) PrimaryMaleHeadPiece else PrimaryFemaleHeadPiece
        return getProperty(property)
    }

    fun getPrimaryModel(sex: Sex): SerializableProperty<PrimaryModel> {
        return getProperty(if (sex.isMale) PrimaryMaleModel else PrimaryFemaleModel)
    }

    fun getSecondaryHeadModel(sex: Sex): SerializableProperty<Int> {
        val property = if (sex.isMale) SecondaryMaleHeadPiece else SecondaryFemaleHeadPiece
        return getProperty(property)
    }

    fun getSecondaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) SecondaryMaleModel else SecondaryFemaleModel)
    }

    fun getTertiaryModel(sex: Sex): SerializableProperty<Int> {
        return getProperty(if (sex.isMale) TertiaryMaleModel else TertiaryFemaleModel)
    }

    companion object {

        /**
         * The name of the archive entry containing the item definitions, without the extension.
         */
        const val ENTRY_NAME = "obj"

    }

}
