package rs.emulate.legacy.version

/**
 * A type of an ArchiveEntry in the `versionlist` Archive.
 */
interface VersionEntryType {

    /**
     * The name of the ArchiveEntry.
     */
    val fileName: String

    /**
     * Gets the name of the ArchiveEntry, for a [CrcList].
     */
    fun asCrcList(): String {
        return fileName + CrcList.ENTRY_NAME_SUFFIX
    }

    /**
     * Gets the name of the ArchiveEntry, for a [VersionList].
     */
    fun asVersionList(): String {
        return fileName + VersionList.ENTRY_NAME_SUFFIX
    }

}
