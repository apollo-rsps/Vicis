package rs.emulate.legacy.config.npc;

import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

import java.util.Map;

/**
 * A {@link MutableConfigDefinition} for an npc.
 *
 * @author Major
 */
public class NpcDefinition extends MutableConfigDefinition {

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
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public NpcDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the animation set.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<MovementAnimationSet> animationSet() {
		return getProperty(NpcProperty.ANIMATION_SET);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the clickable flag.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Boolean> clickable() {
		return getProperty(NpcProperty.CLICKABLE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the colours.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Map<Integer, Integer>> colours() {
		return getProperty(NpcProperty.COLOURS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the combat level.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> combatLevel() {
		return getProperty(NpcProperty.COMBAT_LEVEL);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the description.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<String> description() {
		return getProperty(NpcProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the flat scale.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> flatScale() {
		return getProperty(NpcProperty.FLAT_SCALE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the head icon.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> headIcon() {
		return getProperty(NpcProperty.HEAD_ICON);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the height scale.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> heightScale() {
		return getProperty(NpcProperty.HEIGHT_SCALE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the idle animation.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> idleAnimation() {
		return getProperty(NpcProperty.IDLE_ANIMATION);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the light modifier.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> lightModifier() {
		return getProperty(NpcProperty.LIGHT_MODIFIER);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the minimap visible flag.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Boolean> minimapVisible() {
		return getProperty(NpcProperty.MINIMAP_VISIBLE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the models.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<int[]> models() {
		return getProperty(NpcProperty.MODELS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the {@link MorphismSet}.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<MorphismSet> morphismSet() {
		return getProperty(NpcProperty.MORPHISM_SET);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the name.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<String> name() {
		return getProperty(NpcProperty.NAME);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the priority render flag.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Boolean> priorityRender() {
		return getProperty(NpcProperty.PRIORITY_RENDER);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the rotation.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> rotation() {
		return getProperty(NpcProperty.ROTATION);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the secondary models.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<int[]> secondaryModels() {
		return getProperty(NpcProperty.SECONDARY_MODELS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the shadow modifier.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> shadowModifier() {
		return getProperty(NpcProperty.SHADOW_MODIFIER);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the size.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> size() {
		return getProperty(NpcProperty.SIZE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the walking animation.
	 *
	 * @return The property.
	 */
	public final SerializableProperty<Integer> walkingAnimation() {
		return getProperty(NpcProperty.WALKING_ANIMATION);
	}

}