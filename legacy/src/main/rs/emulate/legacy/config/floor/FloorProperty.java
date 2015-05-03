package rs.emulate.legacy.config.floor;

import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * A {@link ConfigPropertyType} implementation for {@link FloorDefinition}s.
 *
 * @author Major
 */
public enum FloorProperty implements ConfigPropertyType {

	/**
	 * The colour FloorProperty.
	 */
	COLOUR(1),

	/**
	 * The texture id FloorProperty.
	 */
	TEXTURE(2),

	/**
	 * The shadowed FloorProperty.
	 */
	SHADOWED(5),

	/**
	 * The name FloorProperty.
	 */
	NAME(6),

	/**
	 * The minimap colour FloorProperty.
	 */
	MINIMAP_COLOUR(7);

	/**
	 * The opcode of this FloorProperty.
	 */
	private final int opcode;

	/**
	 * Creates the FloorProperty.
	 *
	 * @param opcode The opcode of the FloorProperty.
	 */
	FloorProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int opcode() {
		return opcode;
	}

}