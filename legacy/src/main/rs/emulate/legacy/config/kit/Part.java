package rs.emulate.legacy.config.kit;

import java.util.Arrays;
import java.util.NoSuchElementException;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

/**
 * The body part an {@link IdentikitDefinition} is for.
 *
 * @author Major
 */
enum Part {

	/**
	 * The null Part, used as the default.
	 */
	NULL(-1),

	/**
	 * The head Part.
	 */
	HEAD(0),

	/**
	 * The chin Part.
	 */
	CHIN(1),

	/**
	 * The chest Part.
	 */
	CHEST(2),

	/**
	 * The arms Part.
	 */
	ARMS(3),

	/**
	 * The hands Part.
	 */
	HANDS(4),

	/**
	 * The legs Part.
	 */
	LEGS(5),

	/**
	 * The feet Part.
	 */
	FEET(6);

	/**
	 * The id offset for female characters.
	 */
	private static final int FEMALE_ID_OFFSET = 7;

	/**
	 * Decodes a Part from the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @return The Part.
	 */
	public static Part decode(DataBuffer buffer) {
		return Part.valueOf(buffer.getUnsignedByte());
	}

	/**
	 * Encodes the specified Part into the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @param part The Part.
	 */
	public static void encode(DataBuffer buffer, Part part) {
		buffer.putByte(part.id);
	}

	/**
	 * Gets the Part with the specified id.
	 *
	 * @param value The id.
	 * @return The Part.
	 * @throws NoSuchElementException If no Part with the specified id exists.
	 */
	public static Part valueOf(int value) {
		Assertions.checkWithin(0, 13, value, "Part value must be [0, 13] (received " + value + ").");
		Part[] values = values();
		int id = value % values.length; // 0-6 are male, 7-13 are female.

		return Arrays.stream(values).filter(part -> part.id == id).findAny().get();
	}

	/**
	 * The id of this Part.
	 */
	private final int id;

	/**
	 * Creates the Part.
	 *
	 * @param id The id of the Part.
	 */
	Part(int id) {
		this.id = id;
	}

	/**
	 * Gets the id of this Part for female characters.
	 *
	 * @return The id.
	 */
	public int getFemaleId() {
		return id + FEMALE_ID_OFFSET;
	}

	/**
	 * Gets the id of this Part for male characters.
	 *
	 * @return The id.
	 */
	public int getMaleId() {
		return id;
	}

}