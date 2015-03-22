package rs.emulate.legacy.config.varp;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * Contains {@link ConfigPropertyType} implementations for {@link ParameterVariableDefinition}s.
 * 
 * @author Major
 */
enum ParameterVariableProperty implements ConfigPropertyType {

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
	public int opcode() {
		return opcode;
	}

}