package rs.emulate.legacy.model;

import com.google.common.primitives.Ints;

import java.util.BitSet;

/**
 * A bit-packed set of attributes a Model may have.
 *
 * @author Major
 */
public final class ModelAttributes {

	/**
	 * The bit that indicates the Model these attributes belong to is mandatory (i.e. must be loaded before the client
	 * can continue to the login screen).
	 */
	private static final int MANDATORY = 0; // TODO this is probably a specific type of model

	/**
	 * The bit that indicates the Model these attributes belong to has the maximum priority when loaded as an extra
	 * file.
	 */
	private static final int MAXIMUM_PRIORITY = 3;

	/**
	 * The BitSet containing the attributes.
	 */
	private final BitSet bits;

	/**
	 * Creates the ModelAttributes.
	 *
	 * @param attributes The bit-packed attributes.
	 */
	public ModelAttributes(int attributes) {
		bits = BitSet.valueOf(Ints.toByteArray(attributes));
	}

	/**
	 * Gets the value of the attribute stored in the specified bit, (where the first bit is 0), as a boolean.
	 *
	 * @param bit The bit of the attribute to get. Must be positive.
	 * @return {@code true} if the specified bit has a value of 1, {@code false} if not.
	 */
	public boolean get(int bit) {
		return bits.get(bit);
	}

	/**
	 * Returns whether or not the Model this set of attributes belongs to is a mandatory file (i.e. must be present
	 * before the client will fully load).
	 *
	 * @return {@code true} if the Model is mandatory, {@code false} if not.
	 */
	public boolean mandatory() {
		return get(MANDATORY);
	}

	/**
	 * Gets the request priority of the Model this set of attributes belongs to.
	 * <p>
	 * Despite bit 0 having the lowest priority, if it is set the Model must be present before the client can continue
	 * loading (i.e. it is a mandatory file). The rest of the bits indicate the priority used when requesting the
	 * "extra" (non-mandatory) files, which range from 0 (lowest) to 10 (highest).
	 *
	 * @return The request priority.
	 */
	public int priority() {
		if (bits.get(MAXIMUM_PRIORITY)) {
			return 10;
		} else if (bits.get(5)) {
			return 9;
		} else if (bits.get(4)) {
			return 8;
		} else if (bits.get(6)) {
			return 7;
		} else if (bits.get(7)) {
			return 6;
		} else if (bits.get(1)) {
			return 5;
		} else if (bits.get(2)) {
			return 4;
		} else if (bits.get(MANDATORY)) {
			return 3;
		}

		// TODO methods for these flags
		// bits 1, 5, 6 cause the client to redraw the tabs and dialogue box when the model is loaded.
		// bits 0, 3, 4, 5, and 6 have their model header set to null when the client loads a new region.

		return 0;
	}

	/**
	 * Sets the value of the attribute stored in the specified bit.
	 *
	 * @param bit The bit of the attribute to set. Must be positive.
	 * @param value The value to set, where {@code true} sets a value of 1, and {@code false} sets a value of 0.
	 */
	public void set(int bit, boolean value) {
		bits.set(bit, value);
	}

}