package rs.emulate.legacy.config.npc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import rs.emulate.shared.util.DataBuffer;

import java.util.Arrays;

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
		int varbit = buffer.getUnsignedShort();
		int varp = buffer.getUnsignedShort();
		int count = buffer.getUnsignedByte();
		int[] morphisms = new int[count + 1];

		Arrays.setAll(morphisms, index -> buffer.getUnsignedShort());
		return new MorphismSet(varbit, varp, morphisms);
	}

	/**
	 * Encodes the specified {@link MorphismSet} into the specified {@link DataBuffer}.
	 *
	 * @param buffer The Buffer.
	 * @param set The MorphismSet.
	 */
	public static void encode(DataBuffer buffer, MorphismSet set) {
		buffer.putShort(set.getBitVariable()).putShort(set.getParameterVariable()).putByte(set.getCount() - 1);
		Arrays.stream(set.getMorphisms()).forEach(buffer::putShort);
	}

	/**
	 * The morphisms.
	 */
	private final int[] morphisms;

	/**
	 * The morphism variable bit.
	 */
	private final int varbit;

	/**
	 * The morphism variable parameter.
	 */
	private final int varp;

	/**
	 * Creates the MorphismSet.
	 *
	 * @param varbit The morphism variable bit.
	 * @param varp The morphism variable parameter.
	 * @param morphisms The morphisms. Must not be {@code null}.
	 */
	public MorphismSet(int varbit, int varp, int[] morphisms) {
		Preconditions.checkNotNull(morphisms, "Morphisms array must not be null.");

		this.varbit = varbit;
		this.varp = varp;
		this.morphisms = morphisms;
	}

	/**
	 * Gets the morphism bit variable id.
	 *
	 * @return The bit variable id.
	 */
	public int getBitVariable() {
		return varbit;
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
	 * Gets the morphism variable parameter id.
	 *
	 * @return The variable parameter id.
	 */
	public int getParameterVariable() {
		return varp;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("bit variable", varbit).add("parameter variable", varp)
				.add("morphisms", Arrays.toString(morphisms)).toString();
	}

}