package rs.emulate.legacy.config.item;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for {@link ItemDefinition}s.
 *
 * @author Major
 */
public enum ItemProperty implements ConfigPropertyType {

	/**
	 * The model id property.
	 */
	MODEL(1),

	/**
	 * The name property.
	 */
	NAME(2),

	/**
	 * The description property.
	 */
	DESCRIPTION(3),

	/**
	 * The sprite scale property.
	 */
	SPRITE_SCALE(4),

	/**
	 * The sprite pitch property.
	 */
	SPRITE_PITCH(5),

	/**
	 * The sprite camera roll property.
	 */
	SPRITE_CAMERA_ROLL(6),

	/**
	 * The horizontal sprite translation property.
	 */
	SPRITE_TRANSLATE_X(7),

	/**
	 * The vertical sprite translation property.
	 */
	SPRITE_TRANSLATE_Y(8),

	/**
	 * The stackable property.
	 */
	STACKABLE(11),

	/**
	 * The value property.
	 */
	VALUE(12),

	/**
	 * The members-only property.
	 */
	MEMBERS(16),

	/**
	 * The primary male model id property.
	 */
	PRIMARY_MALE_MODEL(23),

	/**
	 * The secondary male model id property.
	 */
	SECONDARY_MALE_MODEL(24),

	/**
	 * The primary female model id property.
	 */
	PRIMARY_FEMALE_MODEL(25),

	/**
	 * The secondary female model id property.
	 */
	SECONDARY_FEMALE_MODEL(26),

	/**
	 * The colours property.
	 */
	COLOURS(40),

	/**
	 * The tertiary male model id property.
	 */
	TERTIARY_MALE_MODEL(78),

	/**
	 * The tertiary female model id property.
	 */
	TERTIARY_FEMALE_MODEL(79),

	/**
	 * The primary male head piece id property.
	 */
	PRIMARY_MALE_HEAD_PIECE(90),

	/**
	 * The primary female head piece id property.
	 */
	PRIMARY_FEMALE_HEAD_PIECE(91),

	/**
	 * The secondary male head piece id property.
	 */
	SECONDARY_MALE_HEAD_PIECE(92),

	/**
	 * The secondary female head piece id property.
	 */
	SECONDARY_FEMALE_HEAD_PIECE(93),

	/**
	 * The sprite camera yaw property.
	 */
	SPRITE_CAMERA_YAW(95),

	/**
	 * The note info item id.
	 */
	NOTE_INFO_ID(97),

	/**
	 * The note template item id.
	 */
	NOTE_TEMPLATE_ID(98),

	/**
	 * The ground model width scale property.
	 */
	GROUND_SCALE_X(110),

	/**
	 * The ground model length scale property.
	 */
	GROUND_SCALE_Y(111),

	/**
	 * The ground model height scale property.
	 */
	GROUND_SCALE_Z(112),

	/**
	 * The light ambience property.
	 */
	AMBIENCE(113),

	/**
	 * The light diffusion property.
	 */
	CONTRAST(114),

	/**
	 * The team property.
	 */
	TEAM(115);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the ItemProperty.
	 *
	 * @param opcode The opcode.
	 */
	private ItemProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int opcode() {
		return opcode;
	}

}