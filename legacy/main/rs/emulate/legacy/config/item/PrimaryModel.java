package rs.emulate.legacy.config.item;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

/**
 * A utility class containing a model-id/translation pair.
 * 
 * @author Major
 */
public final class PrimaryModel {

	/**
	 * The empty PrimaryModel.
	 */
	public static final PrimaryModel EMPTY = new PrimaryModel();

	/**
	 * Decodes a PrimaryModel from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @return The PrimaryModel.
	 */
	static PrimaryModel decode(DataBuffer buffer) {
		int id = buffer.getUnsignedShort();
		int translation = buffer.getByte();
		return new PrimaryModel(id, translation);
	}

	/**
	 * Encodes the specified PrimaryModel into the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @param model The PrimaryModel.
	 */
	static void encode(DataBuffer buffer, PrimaryModel model) {
		buffer.putShort(model.getId());
		buffer.putByte(model.getTranslation());
	}

	/**
	 * The id of the model.
	 */
	private final int id;

	/**
	 * The vertical translation applied to the model.
	 */
	private final int translation;

	/**
	 * Creates the PrimaryModel with the specified id and translation.
	 * 
	 * @param id The id of the model.
	 * @param translation The vertical translation applied to the model.
	 */
	public PrimaryModel(int id, int translation) {
		Assertions.checkNonNegative(id, "Id cannot be negative.");
		Assertions.checkShort(translation, "Translation must fit in 16 bits.");

		this.id = id;
		this.translation = translation;
	}

	/**
	 * Creates the PrimaryModel with default values.
	 */
	private PrimaryModel() {
		id = -1;
		translation = 0;
	}

	/**
	 * Gets the id of this model.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the vertical translation of this model.
	 * 
	 * @return The translation.
	 */
	public int getTranslation() {
		return translation;
	}

}