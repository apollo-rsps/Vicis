package rs.emulate.legacy.config.varp;

import rs.emulate.shared.prop.PropertyType;

/**
 * Contains {@link PropertyType} implementations for {@link ParameterVariableDefinition}s.
 * 
 * @author Major
 */
enum ParameterVariableProperty implements PropertyType {

	/**
	 * The parameter property.
	 */
	PARAMETER(1);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the ParameterVariableProperty.
	 * 
	 * @param opcode The opcode.
	 */
	private ParameterVariableProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int getOpcode() {
		return opcode;
	}

}