package rs.emulate.legacy.config.object;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

import java.util.Arrays;

/**
 * A set of an object's possible model ids and positions. This class is immutable.
 *
 * @author Major
 */
public final class ModelSet {

	/**
	 * The empty ModelSet, to be used as a default value.
	 */
	public static final ModelSet EMPTY = new ModelSet(new int[0], new int[0]);

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
	 * Encodes the specified {@link ModelSet} into the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer. Must not be {@code null}.
	 * @param models The ModelSet. Must not be {@code null}.
	 */
	public static void encode(DataBuffer buffer, ModelSet models) {
		int count = models.getCount();
		buffer.putByte(count);

		int[] positions = models.getType();
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
	 * The model types.
	 */
	private final int[] types;

	/**
	 * Creates the ModelSet, with an empty array for the types.
	 *
	 * @param models The array of model ids. Cannot be null.
	 * @throws NullPointerException If the {@code models} array array is null.
	 */
	public ModelSet(int[] models) {
		Preconditions.checkNotNull(models, "Models array cannot be null.");
		this.models = models;
		types = new int[0];
	}

	/**
	 * Creates the ModelSet. The length of the {@code models} and {@code types} arrays must be equal.
	 *
	 * @param models The array of model ids. Cannot be null.
	 * @param types The array of model types. Cannot be null.
	 * @throws IllegalArgumentException If the {@code models} and {@code types} arrays are not of equal length.
	 * @throws NullPointerException If either the {@code models} array or the {@code types} array is null.
	 */
	public ModelSet(int[] models, int[] types) {
		Assertions.checkNonNull("Neither the models nor types array may be null.", models, types);
		Preconditions.checkArgument(models.length == types.length, "Models and types arrays must have an equal length.");

		this.models = models.clone();
		this.types = types.clone();
	}

	/**
	 * Gets the amount of models in this ModelSet.
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
	 * Gets the type at the specified index.
	 *
	 * @param index The index.
	 * @return The type.
	 */
	public int getType(int index) {
		return types[index];
	}

	/**
	 * Gets the model types.
	 *
	 * @return The model types.
	 */
	public int[] getType() {
		return types.clone();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("ids", Arrays.toString(models)).add("types", Arrays.toString(types))
				.toString();
	}

}