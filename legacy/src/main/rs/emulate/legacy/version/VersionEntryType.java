package rs.emulate.legacy.version;

/**
 * A type of an ArchiveEntry in the {@code versionlist} Archive.
 *
 * @author Major
 */
@FunctionalInterface
public interface VersionEntryType {

	/**
	 * Gets the name of the ArchiveEntry, for a {@link CrcList}.
	 *
	 * @return The name.
	 */
	public default String asCrcList() {
		return getName() + CrcList.ENTRY_NAME_SUFFIX;
	}

	/**
	 * Gets the name of the ArchiveEntry, for a {@link VersionList}.
	 *
	 * @return The name.
	 */
	public default String asVersionList() {
		return getName() + VersionList.ENTRY_NAME_SUFFIX;
	}

	/**
	 * Gets the name of the ArchiveEntry.
	 *
	 * @return The name of the ArchiveEntry.
	 */
	public String getName();

}