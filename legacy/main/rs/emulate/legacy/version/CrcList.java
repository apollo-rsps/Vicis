package rs.emulate.legacy.version;

import com.google.common.base.Preconditions;

/**
 * A list of file CRCs, for a particular ArchiveEntry (such as models).
 * <p>
 * This class is immutable.
 *
 * @author Major
 */
public final class CrcList {

	/**
	 * The file CRCs.
	 */
	private final int[] crcs;

	/**
	 * Creates the CrcList.
	 *
	 * @param crcs The file CRCs. Further changes to this array will not affect this list.
	 */
	public CrcList(int[] crcs) {
		this.crcs = crcs.clone();
	}

	/**
	 * Gets the CRC of the specified file.
	 * 
	 * @param file The file.
	 * @return The CRC.
	 */
	public int getCrc(int file) {
		Preconditions.checkElementIndex(file, crcs.length, "CRC file id out of bounds.");
		return crcs[file];
	}

	/**
	 * Gets the array of file CRCs. Changes to this array will not affect this list.
	 * 
	 * @return The CRCs.
	 */
	public int[] getCrcs() {
		return crcs.clone();
	}

	/**
	 * Gets the amount of file CRCs.
	 * 
	 * @return The amount.
	 */
	public int size() {
		return crcs.length;
	}

}