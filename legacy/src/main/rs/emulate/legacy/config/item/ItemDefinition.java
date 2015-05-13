package rs.emulate.legacy.config.item;

import java.util.Map;

import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.shared.world.Sex;
import rs.emulate.util.Assertions;

/**
 * A definition for an item (an 'obj' in the cache).
 * 
 * @author Major
 */
public class ItemDefinition extends MutableConfigDefinition {

	/**
	 * The name of the archive entry containing the item definitions, without the extension.
	 */
	public static final String ENTRY_NAME = "obj";

	/**
	 * Checks that the specified index is between 0 and {@code upper}, inclusive.
	 * 
	 * @param upper The upper bound, inclusive.
	 * @param index The index.
	 * @throws IllegalArgumentException If the specified index is not between 0 and {@code upper}.
	 */
	private static void checkBounds(int upper, int index) {
		Assertions.checkWithin(0, upper, index, "Index must be non-negative and less than or equal to " + index + ".");
	}

	/**
	 * Creates the ItemDefinition.
	 * 
	 * @param id The id.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public ItemDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the {@link Map} of original to replacement colours.
	 * 
	 * @return The property.
	 */
	public SerializableProperty<Map<Integer, Integer>> getColours() {
		return getProperty(ItemProperty.COLOURS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the description.
	 *
	 * @return The property.
	 */
	public SerializableProperty<String> getDescription() {
		return getProperty(ItemProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the ground action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public SerializableProperty<String> getGroundAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(ConfigUtils.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link SerializableProperty} containing the scale, in the x-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public SerializableProperty<Integer> getGroundScaleX() {
		return getProperty(ItemProperty.GROUND_SCALE_X);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the scale, in the y-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public SerializableProperty<Integer> getGroundScaleY() {
		return getProperty(ItemProperty.GROUND_SCALE_Y);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the scale, in the z-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public SerializableProperty<Integer> getGroundScaleZ() {
		return getProperty(ItemProperty.GROUND_SCALE_Z);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the inventory action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public SerializableProperty<String> getInventoryAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link SerializableProperty} containing the item stack of the specified index.
	 * 
	 * @param index The index.
	 * @return The property.
	 */
	public SerializableProperty<ItemStack> getItemStack(int index) {
		checkBounds(ItemConstants.ITEM_STACK_COUNT, index);
		return getProperty(ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link SerializableProperty} containing the light ambiance.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getLightAmbiance() {
		return getProperty(ItemProperty.AMBIENCE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the light diffusion.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getLightDiffusion() {
		return getProperty(ItemProperty.CONTRAST);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the model id.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getModelId() {
		return getProperty(ItemProperty.MODEL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the name.
	 *
	 * @return The property.
	 */
	public SerializableProperty<String> getName() {
		return getProperty(ItemProperty.NAME);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the note info id.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getNoteInfoId() {
		return getProperty(ItemProperty.NOTE_INFO_ID);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the note template id.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getNoteTemplateId() {
		return getProperty(ItemProperty.NOTE_TEMPLATE_ID);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public SerializableProperty<Integer> getPrimaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_HEAD_PIECE : ItemProperty.PRIMARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the {@link PrimaryModel} for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public SerializableProperty<Integer> getPrimaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_MODEL : ItemProperty.PRIMARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSecondaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_HEAD_PIECE : ItemProperty.SECONDARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the secondary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSecondaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_MODEL : ItemProperty.SECONDARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite camera roll.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpriteCameraRoll() {
		return getProperty(ItemProperty.SPRITE_CAMERA_ROLL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite camera yaw.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpriteCameraYaw() {
		return getProperty(ItemProperty.SPRITE_CAMERA_YAW);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite pitch.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpritePitch() {
		return getProperty(ItemProperty.SPRITE_PITCH);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite scale.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpriteScale() {
		return getProperty(ItemProperty.SPRITE_SCALE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite translate x.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpriteTranslateX() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_X);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the sprite translate y.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getSpriteTranslateY() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_Y);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the team.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getTeam() {
		return getProperty(ItemProperty.TEAM);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the tertiary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public SerializableProperty<Integer> getTertiaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.TERTIARY_MALE_MODEL : ItemProperty.TERTIARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the value.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Integer> getValue() {
		return getProperty(ItemProperty.VALUE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the members flag.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Boolean> isMembers() {
		return getProperty(ItemProperty.MEMBERS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the stackable flag.
	 *
	 * @return The property.
	 */
	public SerializableProperty<Boolean> isStackable() {
		return getProperty(ItemProperty.STACKABLE);
	}

}