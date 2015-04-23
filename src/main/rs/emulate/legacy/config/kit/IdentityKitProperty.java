package rs.emulate.legacy.config.kit;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for {@link IdentityKitDefinition}s.
 * 
 * @author Major
 */
enum IdentityKitProperty implements ConfigPropertyType {

	/**
	 * The IdentityKitProperty that specifies which body part the identity kit is for.
	 */
	PART(1),

	/**
	 * The IdentityKitProperty that specifies the body model ids of the identity kit. 
	 */
	MODELS(2),

	/**
	 * The IdentityKitProperty that specifies whether or not the identity kit may be used as a player design style.
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