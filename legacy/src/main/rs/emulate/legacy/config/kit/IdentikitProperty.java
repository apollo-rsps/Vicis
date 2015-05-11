package rs.emulate.legacy.config.kit;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for {@link IdentikitDefinition}s.
 *
 * @author Major
 */
enum IdentikitProperty implements ConfigPropertyType {

	/**
	 * The IdentikitProperty that specifies which body part the identikit is for.
	 */
	PART(1),

	/**
	 * The IdentikitProperty that specifies the body model ids of the identikit.
	 */
	MODELS(2),

	/**
	 * The IdentikitProperty that specifies whether or not the identikit may be used as a player design style.
	 */
	PLAYER_DESIGN_STYLE(3);

	/**
	 * The opcode of this IdentikitProperty.
	 */
	private final int opcode;

	/**
	 * Creates the IdentikitProperty.
	 *
	 * @param opcode The opcode.
	 */
	IdentikitProperty(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Gets the opcode of this IdentikitProperty.
	 *
	 * @return The opcode.
	 */
	@Override
	public int opcode() {
		return opcode;
	}

}