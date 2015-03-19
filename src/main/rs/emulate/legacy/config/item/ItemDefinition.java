package rs.emulate.legacy.config.item;

import java.util.Map;

import rs.emulate.legacy.config.DefinitionUtils;
import rs.emulate.legacy.config.MutableDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.world.Sex;
import rs.emulate.util.Assertions;

/**
 * A definition for an item (an 'obj' in the cache).
 * 
 * @author Major
 */
public class ItemDefinition extends MutableDefinition {

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
	 * Creates the item definition.
	 * 
	 * @param id The id.
	 * @param properties The {@link PropertyMap}.
	 */
	public ItemDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the {@link Map} of original to replacement colours.
	 * 
	 * @return The property.
	 */
	public DefinitionProperty<Map<Integer, Integer>> getColours() {
		return getProperty(ItemProperty.COLOURS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the description.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<String> getDescription() {
		return getProperty(ItemProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the ground action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public DefinitionProperty<String> getGroundAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(DefinitionUtils.createOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the scale, in the x-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getGroundScaleX() {
		return getProperty(ItemProperty.GROUND_SCALE_X);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the scale, in the y-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getGroundScaleY() {
		return getProperty(ItemProperty.GROUND_SCALE_Y);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the scale, in the z-direction, of this item's model when displayed
	 * on the ground.
	 * 
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getGroundScaleZ() {
		return getProperty(ItemProperty.GROUND_SCALE_Z);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the inventory action for the specified index.
	 * 
	 * @param index The action index.
	 * @return The property.
	 */
	public DefinitionProperty<String> getInventoryAction(int index) {
		checkBounds(ItemConstants.MENU_ACTION_COUNT, index);
		return getProperty(DefinitionUtils.createOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the item stack of the specified index.
	 * 
	 * @param index The index.
	 * @return The property.
	 */
	public DefinitionProperty<ItemStack> getItemStack(int index) {
		checkBounds(ItemConstants.ITEM_STACK_COUNT, index);
		return getProperty(DefinitionUtils.createOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, index));
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the light ambiance.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getLightAmbiance() {
		return getProperty(ItemProperty.LIGHT_AMBIENCE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the light diffusion.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getLightDiffusion() {
		return getProperty(ItemProperty.LIGHT_DIFFUSION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the model id.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getModelId() {
		return getProperty(ItemProperty.MODEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the name.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<String> getName() {
		return getProperty(ItemProperty.NAME);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the note info id.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getNoteInfoId() {
		return getProperty(ItemProperty.NOTE_INFO_ID);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the note template id.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getNoteTemplateId() {
		return getProperty(ItemProperty.NOTE_TEMPLATE_ID);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getPrimaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_HEAD_PIECE : ItemProperty.PRIMARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the {@link PrimaryModel} for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public DefinitionProperty<PrimaryModel> getPrimaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.PRIMARY_MALE_MODEL : ItemProperty.PRIMARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the head model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSecondaryHeadModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_HEAD_PIECE : ItemProperty.SECONDARY_FEMALE_HEAD_PIECE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the secondary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSecondaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.SECONDARY_MALE_MODEL : ItemProperty.SECONDARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite camera roll.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpriteCameraRoll() {
		return getProperty(ItemProperty.SPRITE_CAMERA_ROLL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite camera yaw.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpriteCameraYaw() {
		return getProperty(ItemProperty.SPRITE_CAMERA_YAW);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite pitch.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpritePitch() {
		return getProperty(ItemProperty.SPRITE_PITCH);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite scale.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpriteScale() {
		return getProperty(ItemProperty.SPRITE_SCALE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite translate x.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpriteTranslateX() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_X);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the sprite translate y.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getSpriteTranslateY() {
		return getProperty(ItemProperty.SPRITE_TRANSLATE_Y);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the team.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getTeam() {
		return getProperty(ItemProperty.TEAM);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the tertiary model id for the specified {@link Sex}.
	 * 
	 * @param sex The sex.
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getTertiaryModel(Sex sex) {
		return getProperty(sex.isMale() ? ItemProperty.TERTIARY_MALE_MODEL : ItemProperty.TERTIARY_FEMALE_MODEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the value.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Integer> getValue() {
		return getProperty(ItemProperty.VALUE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the members flag.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Boolean> isMembers() {
		return getProperty(ItemProperty.MEMBERS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the stackable flag.
	 *
	 * @return The property.
	 */
	public DefinitionProperty<Boolean> isStackable() {
		return getProperty(ItemProperty.STACKABLE);
	}

}