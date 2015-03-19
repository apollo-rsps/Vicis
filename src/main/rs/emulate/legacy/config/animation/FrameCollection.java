package rs.emulate.legacy.config.animation;

import java.util.Arrays;

import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * A collection of data about frames used in the animation.
 * 
 * @author Major
 */
public final class FrameCollection {

	/**
	 * The empty FrameCollection, used as a default value.
	 */
	public static final FrameCollection EMPTY = new FrameCollection();

	/**
	 * The integer value that indicates a secondary frame id does not exist.
	 */
	private static final int NULL_SECONDARY_ID = 65_535;

	/**
	 * Decodes a {@link FrameCollection} from the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @return The FrameCollection.
	 */
	public static FrameCollection decode(DataBuffer buffer) {
		int frames = buffer.getUnsignedByte();
		int[] primaries = new int[frames], secondaries = new int[frames], durations = new int[frames];

		for (int frame = 0; frame < frames; frame++) {
			primaries[frame] = buffer.getUnsignedShort();
			int secondary = buffer.getUnsignedShort();
			secondaries[frame] = (secondary == NULL_SECONDARY_ID) ? -1 : secondary;
			durations[frame] = buffer.getUnsignedShort();
		}

		return new FrameCollection(primaries, secondaries, durations);
	}

	/**
	 * Encodes the specified {@link FrameCollection} into the specified {@link DataBuffer}.
	 * 
	 * @param buffer The Buffer.
	 * @param collection The FrameCollection.
	 */
	public static void encode(DataBuffer buffer, FrameCollection collection) {
		int size = collection.getSize();
		buffer.putByte(size);

		for (int index = 0; index < size; index++) {
			buffer.putShort(collection.getPrimary(index)).putShort(collection.getSecondary(index));
			buffer.putShort(collection.getDuration(index));
		}
	}

	/**
	 * The frame durations.
	 */
	private final int[] durations;

	/**
	 * The primary frame ids.
	 */
	private final int[] primaries;

	/**
	 * The secondary frame ids.
	 */
	private final int[] secondaries;

	/**
	 * Creates the FrameCollection.
	 *
	 * @param primaries The primary frame ids.
	 * @param secondaries The secondary frame ids.
	 * @param durations The frame durations.
	 */
	public FrameCollection(int[] primaries, int[] secondaries, int[] durations) {
		Preconditions.checkArgument(primaries.length == secondaries.length && primaries.length == durations.length,
				"Frame collection arays must all be of the same length.");

		this.primaries = primaries;
		this.secondaries = secondaries;
		this.durations = durations;
	}

	/**
	 * Private constructor used to create a default (i.e. empty) FrameCollection.
	 */
	private FrameCollection() {
		primaries = secondaries = durations = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FrameCollection) {
			FrameCollection other = (FrameCollection) obj;
			if (!Arrays.equals(primaries, other.primaries) || !Arrays.equals(secondaries, other.secondaries)) {
				return false;
			}

			return Arrays.equals(durations, other.durations);
		}

		return false;
	}

	/**
	 * Gets the duration at the specified index.
	 * 
	 * @param index The index.
	 * @return The duration.
	 */
	public int getDuration(int index) {
		return durations[index];
	}

	/**
	 * Gets the frame durations of this collection.
	 *
	 * @return The frame durations.
	 */
	public int[] getDurations() {
		return durations.clone();
	}

	/**
	 * Gets the primary frame ids of this collection.
	 *
	 * @return The primary frame ids.
	 */
	public int[] getPrimaries() {
		return primaries.clone();
	}

	/**
	 * Gets the primary frame id at the specified index.
	 * 
	 * @param index The index.
	 * @return The primary frame id.
	 */
	public int getPrimary(int index) {
		return primaries[index];
	}

	/**
	 * Gets the secondary frame ids of this collection.
	 *
	 * @return The secondary frame ids.
	 */
	public int[] getSecondaries() {
		return secondaries.clone();
	}

	/**
	 * Gets the secondary frame id at the specified index.
	 * 
	 * @param index The index.
	 * @return The secondary frame id.
	 */
	public int getSecondary(int index) {
		return secondaries[index];
	}

	/**
	 * Gets the size of this collection.
	 *
	 * @return The size.
	 */
	public int getSize() {
		return primaries.length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + Arrays.hashCode(primaries);
		result = prime * result + Arrays.hashCode(durations);
		return prime * result + Arrays.hashCode(secondaries);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("Primary frames", Arrays.toString(primaries))
				.add("Secondary frames", Arrays.toString(secondaries)).add("Frame durations", Arrays.toString(durations))
				.toString();
	}

}