package rs.emulate.legacy.config.animation;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;

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
	 * Gets the {@link ConfigProperty} containing the animating precedence of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> animatingPrecedence() {
		return getProperty(AnimationProperty.ANIMATING_PRECEDENCE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the interleave order of this animation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<int[]> getInterleaveOrder() {
		return getProperty(AnimationProperty.INTERLEAVE_ORDER);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link FrameCollection} of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<FrameCollection> frameCollection() {
		return getProperty(AnimationProperty.FRAMES);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the loop offset of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> loopOffset() {
		return getProperty(AnimationProperty.LOOP_OFFSET);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the maximum loop count of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> maximumLoops() {
		return getProperty(AnimationProperty.MAXIMUM_LOOPS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the player mainhand id of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> playerMainhand() {
		return getProperty(AnimationProperty.PLAYER_MAINHAND);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the player offhand id of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> playerOffhand() {
		return getProperty(AnimationProperty.PLAYER_OFFHAND);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the priority of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> priority() {
		return getProperty(AnimationProperty.PRIORITY);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the replay mode of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> replayMode() {
		return getProperty(AnimationProperty.REPLAY_MODE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the stretches property of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Boolean> stretches() {
		return getProperty(AnimationProperty.STRETCHES);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the walking precedence of this animation.
	 *
	 * @return The DefinitionProperty.
	 */
	public final ConfigProperty<Integer> walkingPrecedence() {
		return getProperty(AnimationProperty.WALKING_PRECEDENCE);
	}

}