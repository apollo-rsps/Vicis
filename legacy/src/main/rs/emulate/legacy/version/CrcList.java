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
	 * The name suffix of CrcList entries.
	 */
	static final String ENTRY_NAME_SUFFIX = "_crc";

	/**
	 * The file CRCs.
	 */
	private final int[] crcs;

	/**
	 * The type of this CrcList.
	 */
	private final VersionEntryType type;

	/**
	 * Creates the CrcList.
	 *
	 * @param type The {@link VersionEntryType} of the CrcList.
	 * @param crcs The file CRCs. Further changes to this array will not affect this list.
	 */
	public CrcList(VersionEntryType type, int[] crcs) {
		this.type = type;
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
	 * Gets the {@link VersionEntryType} of this CrcList.
	 *
	 * @return The type.
	 */
	public VersionEntryType getType() {
		return type;
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