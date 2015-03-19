package rs.emulate.legacy.config.npc;

import java.util.Map;

import rs.emulate.legacy.config.MutableDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

/**
 * A {@link MutableDefinition} for an npc.
 * 
 * @author Major
 */
public class NpcDefinition extends MutableDefinition {

	/**
	 * The name of the archive entry containing the NpcDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "npc";

	/**
	 * The amount of interactions.
	 */
	protected static final int INTERACTION_COUNT = 10;

	/**
	 * The prefix used for interaction definition properties.
	 */
	protected static final String INTERACTION_PROPERTY_PREFIX = "interaction";

	/**
	 * Creates the NpcDefinition.
	 * 
	 * @param id The id of the npc.
	 * @param properties The {@link PropertyMap}.
	 */
	public NpcDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the animation set.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<MovementAnimationSet> animationSet() {
		return getProperty(NpcProperty.ANIMATION_SET);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the clickable flag.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Boolean> clickable() {
		return getProperty(NpcProperty.CLICKABLE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the colours.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Map<Integer, Integer>> colours() {
		return getProperty(NpcProperty.COLOURS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the combat level.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> combatLevel() {
		return getProperty(NpcProperty.COMBAT_LEVEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the description.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<String> description() {
		return getProperty(NpcProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the flat scale.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> flatScale() {
		return getProperty(NpcProperty.FLAT_SCALE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the head icon.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> headIcon() {
		return getProperty(NpcProperty.HEAD_ICON);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the height scale.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> heightScale() {
		return getProperty(NpcProperty.HEIGHT_SCALE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the idle animation.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> idleAnimation() {
		return getProperty(NpcProperty.IDLE_ANIMATION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the light modifier.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> lightModifier() {
		return getProperty(NpcProperty.LIGHT_MODIFIER);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the minimap visible flag.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Boolean> minimapVisible() {
		return getProperty(NpcProperty.MINIMAP_VISIBLE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the models.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<int[]> models() {
		return getProperty(NpcProperty.MODELS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the {@link MorphismSet}.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<MorphismSet> morphismSet() {
		return getProperty(NpcProperty.MORPHISM_SET);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the name.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<String> name() {
		return getProperty(NpcProperty.NAME);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the priority render flag.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Boolean> priorityRender() {
		return getProperty(NpcProperty.PRIORITY_RENDER);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the rotation.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> rotation() {
		return getProperty(NpcProperty.ROTATION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the secondary models.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<int[]> secondaryModels() {
		return getProperty(NpcProperty.SECONDARY_MODELS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the shadow modifier.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> shadowModifier() {
		return getProperty(NpcProperty.SHADOW_MODIFIER);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the size.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> size() {
		return getProperty(NpcProperty.SIZE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the walking animation.
	 * 
	 * @return The property.
	 */
	public final DefinitionProperty<Integer> walkingAnimation() {
		return getProperty(NpcProperty.WALKING_ANIMATION);
	}

}