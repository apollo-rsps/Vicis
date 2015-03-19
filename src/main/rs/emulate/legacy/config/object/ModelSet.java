package rs.emulate.legacy.config.object;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A container for an object's possible model ids and positions. This class is immutable.
 * 
 * @author Major
 */
public final class ModelSet {

	/**
	 * The empty model set, to be used as a default value.
	 */
	public static final ModelSet EMPTY = new ModelSet(new int[0], new int[0]);

	/**
	 * Decodes a ModelSet with model positions from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer. Must not be {@code null}.
	 * @return The ModelSet.
	 */
	public static ModelSet decodePositioned(DataBuffer buffer) {
		int count = buffer.getUnsignedByte();
		int[] models = new int[count], positions = new int[count];

		for (int index = 0; index < count; index++) {
			models[index] = buffer.getUnsignedShort();
			positions[index] = buffer.getUnsignedByte();
		}

		return new ModelSet(models, positions);
	}

	/**
	 * Decodes a ModelSet with no model positions from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer. Must not be {@code null}.
	 * @return The ModelSet.
	 */
	public static ModelSet decode(DataBuffer buffer) {
		int count = buffer.getUnsignedByte();
		int[] models = new int[count];

		for (int index = 0; index < count; index++) {
			models[index] = buffer.getUnsignedShort();
		}

		return new ModelSet(models);
	}

	/**
	 * Encodes the specified {@link ModelSet} into the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer. Must not be {@code null}.
	 * @param models The ModelSet. Must not be {@code null}.
	 */
	public static void encode(DataBuffer buffer, ModelSet models) {
		int count = models.getCount();
		buffer.putByte(count);

		int[] positions = models.getPositions();
		boolean positioned = positions.length != 0;

		for (int index = 0; index < count; index++) {
			buffer.putShort(models.getModel(index));

			if (positioned) {
				buffer.putByte(positions[index]);
			}
		}
	}

	/**
	 * The model ids.
	 */
	private final int[] models;

	/**
	 * The model positions.
	 */
	private final int[] positions;

	/**
	 * Creates the model set, with an empty array for the positions.
	 * 
	 * @param models The array of model ids. Cannot be null.
	 * @throws NullPointerException If the {@code models} array array is null.
	 */
	public ModelSet(int[] models) {
		Preconditions.checkNotNull(models, "Models array cannot be null.");
		this.models = models;
		positions = new int[0];
	}

	/**
	 * Creates the model set. The length of the {@code models} and {@code positions} arrays must be equal.
	 * 
	 * @param models The array of model ids. Cannot be null.
	 * @param positions The array of model positions. Cannot be null.
	 * @throws IllegalArgumentException If the {@code model} and {@code position} arrays are not of equal length.
	 * @throws NullPointerException If either the {@code models} array or the {@code positions} array is null.
	 */
	public ModelSet(int[] models, int[] positions) {
		Assertions.checkNonNull("Neither the models nor positions array may be null.", models, positions);
		Preconditions.checkArgument(models.length == positions.length, "Model and position arrays must have equal length.");

		this.models = models.clone();
		this.positions = positions.clone();
	}

	/**
	 * Gets the amount of models in this set.
	 * 
	 * @return The amount.
	 */
	public int getCount() {
		return models.length;
	}

	/**
	 * Gets the model id at the specified index.
	 * 
	 * @param index The index.
	 * @return The model id.
	 */
	public int getModel(int index) {
		return models[index];
	}

	/**
	 * Gets the model ids.
	 * 
	 * @return The model ids.
	 */
	public int[] getModels() {
		return models.clone();
	}

	/**
	 * Gets the position at the specified index.
	 * 
	 * @param index The index.
	 * @return The position.
	 */
	public int getPosition(int index) {
		return positions[index];
	}

	/**
	 * Gets the model positions.
	 * 
	 * @return The model positions.
	 */
	public int[] getPositions() {
		return positions.clone();
	}

}