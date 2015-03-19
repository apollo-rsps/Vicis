package rs.emulate.legacy.config.npc;

import java.util.Arrays;

import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * A set of morphisms for an npc or object.
 * 
 * @author Major
 */
public final class MorphismSet {

	/**
	 * The empty morphisms, used as a default value.
	 */
	public static final MorphismSet EMPTY = new MorphismSet(-1, -1, new int[0]);

	/**
	 * Decodes a {@link MorphismSet} from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @return The MorphismSet.
	 */
	public static MorphismSet decode(DataBuffer buffer) {
		int bitVariable = buffer.getUnsignedShort();
		int variable = buffer.getUnsignedShort();
		int count = buffer.getUnsignedByte();
		int[] morphisms = new int[count + 1];

		Arrays.setAll(morphisms, index -> buffer.getUnsignedShort());
		return new MorphismSet(bitVariable, variable, morphisms);
	}

	/**
	 * Encodes the specified {@link MorphismSet} into the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @param set The MorphismSet.
	 */
	public static void encode(DataBuffer buffer, MorphismSet set) {
		buffer.putShort(set.getBitVariable()).putShort(set.getVariable()).putByte(set.getCount() - 1);
		Arrays.stream(set.getMorphisms()).forEach(buffer::putShort);
	}

	/**
	 * The morphism bit variable index.
	 */
	private final int bitVariable;

	/**
	 * The morphisms.
	 */
	private final int[] morphisms;

	/**
	 * The morphism variable index.
	 */
	private final int variable;

	/**
	 * Creates the MorphismSet.
	 * 
	 * @param bitVariable The bit variable index.
	 * @param variable The variable index.
	 * @param morphisms The morphisms. Must not be {@code null}.
	 */
	public MorphismSet(int bitVariable, int variable, int[] morphisms) {
		Preconditions.checkNotNull(morphisms, "Morphisms array must not be null.");

		this.bitVariable = bitVariable;
		this.variable = variable;
		this.morphisms = morphisms;
	}

	/**
	 * Gets the morphism bit variable index.
	 * 
	 * @return The bit variable index.
	 */
	public int getBitVariable() {
		return bitVariable;
	}

	/**
	 * Gets the amount of morphisms.
	 * 
	 * @return The morphisms count.
	 */
	public int getCount() {
		return morphisms.length;
	}

	/**
	 * Gets the morphisms.
	 * 
	 * @return The morphisms.
	 */
	public int[] getMorphisms() {
		return morphisms;
	}

	/**
	 * Gets the morphism variable index.
	 * 
	 * @return The variable index.
	 */
	public int getVariable() {
		return variable;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("bit variable", bitVariable).add("variable", variable)
				.add("morphisms", Arrays.toString(morphisms)).toString();
	}

}