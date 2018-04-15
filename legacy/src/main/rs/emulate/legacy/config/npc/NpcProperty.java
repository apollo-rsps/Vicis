package rs.emulate.legacy.config.npc;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * Contains {@link ConfigPropertyType} implementations for {@link NpcDefinition}s.
 *
 * @author Major
 */
public enum NpcProperty implements ConfigPropertyType {

	/**
	 * The model ids property.
	 */
	MODELS(1),

	/**
	 * The name property.
	 */
	NAME(2),

	/**
	 * The description property.
	 */
	DESCRIPTION(3),

	/**
	 * The size property.
	 */
	SIZE(12),

	/**
	 * The idle animation id property.
	 */
	IDLE_ANIMATION(13),

	/**
	 * The walking animation id property.
	 */
	WALKING_ANIMATION(14),

	/**
	 * The movement animation ids property.
	 */
	ANIMATION_SET(15),

	/**
	 * The colours property.
	 */
	COLOURS(40),

	/**
	 * The secondary model ids property.
	 */
	SECONDARY_MODELS(60),

	/**
	 * The visible on minimap property.
	 */
	MINIMAP_VISIBLE(93),

	/**
	 * The combat level property.
	 */
	COMBAT_LEVEL(95),

	/**
	 * The flat scale property.
	 */
	FLAT_SCALE(97),

	/**
	 * The height scale property.
	 */
	HEIGHT_SCALE(98),

	/**
	 * The priority render property.
	 */
	PRIORITY_RENDER(99),

	/**
	 * The light modifier property.
	 */
	LIGHT_MODIFIER(100),

	/**
	 * The shadow modifier property.
	 */
	SHADOW_MODIFIER(101),

	/**
	 * The head icon id property.
	 */
	HEAD_ICON(102),

	/**
	 * The rotation property.
	 */
	ROTATION(103),

	/**
	 * The {@link MorphismSet} property.
	 */
	MORPHISM_SET(106),

	/**
	 * The clickable property.
	 */
	CLICKABLE(107);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the NpcProperty.
	 *
	 * @param opcode The opcode.
	 */
	private NpcProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int opcode() {
		return opcode;
	}

}