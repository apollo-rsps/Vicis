package rs.emulate.legacy.config.animation;

import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

/**
 * A {@link MutableConfigDefinition} for an animation.
 *
 * @author Major
 */
public class AnimationDefinition extends MutableConfigDefinition {

	/**
	 * The name of the ArchiveEntry containing the AnimationDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "seq";

	/**
	 * Creates the AnimationDefinition.
	 *
	 * @param id The id of the definition.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public AnimationDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the animating precedence of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> animatingPrecedence() {
		return getProperty(AnimationProperty.ANIMATING_PRECEDENCE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the {@link FrameCollection} of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<FrameCollection> frameCollection() {
		return getProperty(AnimationProperty.FRAMES);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the interleave order of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<int[]> getInterleaveOrder() {
		return getProperty(AnimationProperty.INTERLEAVE_ORDER);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the loop offset of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> loopOffset() {
		return getProperty(AnimationProperty.LOOP_OFFSET);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the maximum loop count of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> maximumLoops() {
		return getProperty(AnimationProperty.MAXIMUM_LOOPS);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the player mainhand id of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> playerMainhand() {
		return getProperty(AnimationProperty.PLAYER_MAINHAND);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the player offhand id of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> playerOffhand() {
		return getProperty(AnimationProperty.PLAYER_OFFHAND);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the priority of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> priority() {
		return getProperty(AnimationProperty.PRIORITY);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the replay mode of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> replayMode() {
		return getProperty(AnimationProperty.REPLAY_MODE);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the stretches property of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Boolean> stretches() {
		return getProperty(AnimationProperty.STRETCHES);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the walking precedence of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final SerializableProperty<Integer> walkingPrecedence() {
		return getProperty(AnimationProperty.WALKING_PRECEDENCE);
	}

}