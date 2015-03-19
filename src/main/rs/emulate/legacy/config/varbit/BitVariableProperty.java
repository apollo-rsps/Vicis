package rs.emulate.legacy.config.varbit;

import rs.emulate.shared.prop.PropertyType;

/**
 * A {@link PropertyType} implementation for bit variables.
 * 
 * @author Major
 */
public enum BitVariableProperty implements PropertyType {

	/**
	 * The variable property.
	 */
	VARIABLE(1);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the bit variable property.
	 * 
	 * @param opcode The opcode of the property.
	 */
	private BitVariableProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int getOpcode() {
		return opcode;
	}

}