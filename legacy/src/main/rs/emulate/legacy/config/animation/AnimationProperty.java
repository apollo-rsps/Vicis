package rs.emulate.legacy.config.animation;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for AnimationDefinitions.
 *
 * @author Major
 */
enum AnimationProperty implements ConfigPropertyType {

	/**
	 * The frames property.
	 */
	FRAMES(1),

	/**
	 * The loop offset property.
	 */
	LOOP_OFFSET(2),

	/**
	 * The interleave order property.
	 */
	INTERLEAVE_ORDER(3),

	/**
	 * The stretches property.
	 */
	STRETCHES(4),

	/**
	 * The priority property.
	 */
	PRIORITY(5),

	/**
	 * The player mainhand property.
	 */
	PLAYER_MAINHAND(6),

	/**
	 * The player offhand property.
	 */
	PLAYER_OFFHAND(7),

	/**
	 * The maximum loops property.
	 */
	MAXIMUM_LOOPS(8),
	/**
	 * The animating precedence property.
	 */
	ANIMATING_PRECEDENCE(9),

	/**
	 * The walking precedence property.
	 */
	WALKING_PRECEDENCE(10),

	/**
	 * The replay mode property.
	 */
	REPLAY_MODE(11);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the property.
	 *
	 * @param opcode The opcode.
	 */
	private AnimationProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int opcode() {
		return opcode;
	}

}