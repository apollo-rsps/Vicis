package rs.emulate.legacy.config.graphic;

import rs.emulate.shared.prop.PropertyType;

/**
 * A {@link PropertyType} implementation for graphic definitions.
 * 
 * @author Major
 */
public enum GraphicProperty implements PropertyType {

	/**
	 * The model id property.
	 */
	MODEL(1),

	/**
	 * The animation id property.
	 */
	ANIMATION(2),

	/**
	 * The breadth scale property.
	 */
	BREADTH_SCALE(4),

	/**
	 * The depth scale property.
	 */
	DEPTH_SCALE(5),

	/**
	 * The rotation property.
	 */
	ROTATION(6),

	/**
	 * The brightness property.
	 */
	BRIGHTNESS(7),

	/**
	 * The shadow property.
	 */
	SHADOW(8);

	/**
	 * The opcode of this property.
	 */
	private final int opcode;

	/**
	 * Creates the GraphicProperty.
	 * 
	 * @param opcode The opcode.
	 */
	private GraphicProperty(int opcode) {
		this.opcode = opcode;
	}

	/**
	 * Gets the opcode of this property.
	 * 
	 * @return The opcode.
	 */
	@Override
	public int getOpcode() {
		return opcode;
	}

}