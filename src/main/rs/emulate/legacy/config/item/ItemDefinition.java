package rs.emulate.legacy.config.item;

import java.util.Map;

import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.shared.prop.PropertyMap;
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
	 * @param properties The {@link PropertyMap}.
	 */
	public ItemDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link Map} of original to replacement colours.
	 * 
	 * @return The property.
	 */
	public ConfigProperty<Map<Integer, Integer>> getColours() {
		return getProperty(ItemProperty.COLOURS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the description.
	 *
	 * @return The property.
	 */
	public ConfigProperty<String> getDescription() {
		return getProperty(ItemProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the ground action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public ConfigProperty<String> getGroundAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(ConfigDefinitionUtils.createOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link ConfigProperty} containing the scale, in the x-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public ConfigProperty<Integer> getGroundScaleX() {
		return getProperty(ItemProperty.GROUND_SCALE_X);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the scale, in the y-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public ConfigProperty<Integer> getGroundScaleY() {
		return getProperty(ItemProperty.GROUND_SCALE_Y);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the scale, in the z-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public ConfigProperty<Integer> getGroundScaleZ() {
		return getProperty(ItemProperty.GROUND_SCALE_Z);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the inventory action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public ConfigProperty<String> getInventoryAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(ConfigDefinitionUtils.createOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link ConfigProperty} containing the item stack of the specified index.
	 * 
	 * @param index The index.
	 * @return The property.
	 */
	public ConfigProperty<ItemStack> getItemStack(int index) {
		checkBounds(ItemConstants.ITEM_STACK_COUNT, index);
		return getProperty(ConfigDefinitionUtils.createOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link ConfigProperty} containing the light ambiance.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getLightAmbiance() {
		return getProperty(ItemProperty.LIGHT_AMBIENCE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the light diffusion.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getLightDiffusion() {
		return getProperty(ItemProperty.LIGHT_DIFFUSION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the model id.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getModelId() {
		return getProperty(ItemProperty.MODEL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the name.
	 *
	 * @return The property.
	 */
	public ConfigProperty<String> getName() {
		return getProperty(ItemProperty.NAME);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the note info id.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getNoteInfoId() {
		return getProperty(ItemProperty.NOTE_INFO_ID);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the note template id.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getNoteTemplateId() {
		return getProperty(ItemProperty.NOTE_TEMPLATE_ID);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public ConfigProperty<Integer> getPrimaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_HEAD_PIECE : ItemProperty.PRIMARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link PrimaryModel} for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public ConfigProperty<Integer> getPrimaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_MODEL : ItemProperty.PRIMARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSecondaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_HEAD_PIECE : ItemProperty.SECONDARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the secondary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSecondaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_MODEL : ItemProperty.SECONDARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite camera roll.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpriteCameraRoll() {
		return getProperty(ItemProperty.SPRITE_CAMERA_ROLL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite camera yaw.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpriteCameraYaw() {
		return getProperty(ItemProperty.SPRITE_CAMERA_YAW);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite pitch.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpritePitch() {
		return getProperty(ItemProperty.SPRITE_PITCH);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite scale.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpriteScale() {
		return getProperty(ItemProperty.SPRITE_SCALE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite translate x.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpriteTranslateX() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_X);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the sprite translate y.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getSpriteTranslateY() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_Y);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the team.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getTeam() {
		return getProperty(ItemProperty.TEAM);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the tertiary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public ConfigProperty<Integer> getTertiaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.TERTIARY_MALE_MODEL : ItemProperty.TERTIARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the value.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Integer> getValue() {
		return getProperty(ItemProperty.VALUE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the members flag.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Boolean> isMembers() {
		return getProperty(ItemProperty.MEMBERS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the stackable flag.
	 *
	 * @return The property.
	 */
	public ConfigProperty<Boolean> isStackable() {
		return getProperty(ItemProperty.STACKABLE);
	}

}