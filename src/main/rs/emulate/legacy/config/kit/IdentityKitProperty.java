package rs.emulate.legacy.config.kit;

import rs.emulate.shared.prop.PropertyType;

/**
 * A {@link PropertyType} implementation for {@link IdentityKitDefinition}s.
 * 
 * @author Major
 */
enum IdentityKitProperty implements PropertyType {

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
	public int getOpcode() {
		return opcode;
	}

}