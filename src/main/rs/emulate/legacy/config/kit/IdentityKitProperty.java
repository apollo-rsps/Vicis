package rs.emulate.legacy.config.kit;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for {@link IdentityKitDefinition}s.
 * 
 * @author Major
 */
enum IdentityKitProperty implements ConfigPropertyType {

	/**
	 * The part IdentityKitProperty.
	 */
	PART(1),

	/**
	 * The models IdentityKitProperty.
	 */
	MODELS(2),

	/**
	 * The player style IdentityKitProperty.
	 */
	PLAYER_DESIGN_STYLE(3);

	/**
	 * The opcode of this IdentityKitProperty.
	 */
	private final int opcode;

	/**
	 * Creates the IdentityKitProperty.
	 * 
	 * @param opcode The opcode.
	 */
	private IdentityKitProperty(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Gets the opcode of this IdentityKitProperty.
	 * 
	 * @return The opcode.
	 */
	@Override
	public int opcode() {
		return opcode;
	}

}