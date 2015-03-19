package rs.emulate.legacy.version;

import com.google.common.base.Preconditions;

/**
 * A list of file versions, for a particular ArchiveEntry (such as models).
 * <p>
 * This class is immutable.
 *
 * @author Major
 */
public final class VersionList {

	/**
	 * The file versions.
	 */
	private final int[] versions;

	/**
	 * Creates the VersionList.
	 *
	 * @param versions The file versions. Further changes to this array will not affect this list.
	 */
	public VersionList(int[] versions) {
		this.versions = versions.clone();
	}

	/**
	 * Gets the version number of the specified file.
	 * 
	 * @param file The file.
	 * @return The version number.
	 */
	public int getVersion(int file) {
		Preconditions.checkElementIndex(file, versions.length, "Version file id out of bounds.");
		return versions[file];
	}

	/**
	 * Gets the array of file versions. Changes to this array will not affect this list.
	 * 
	 * @return The versions.
	 */
	public int[] getVersions() {
		return versions.clone();
	}

	/**
	 * Gets the amount of file versions.
	 * 
	 * @return The amount.
	 */
	public int size() {
		return versions.length;
	}

}