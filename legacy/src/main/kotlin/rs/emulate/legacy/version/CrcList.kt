package rs.emulate.legacy.version

/**
 * A list of file CRCs, for a particular ArchiveEntry (such as models).
 *
 * @param type The [VersionEntryType] of the CrcList.
 * @param crcs The file CRCs. Further changes to this array will not affect this list.
 */
class CrcList(val type: VersionEntryType, crcs: IntArray) {

    val crcs: IntArray = crcs.clone()
        get() = field.clone()

    /**
     * Gets the CRC of the specified file.
     */
    operator fun get(file: Int): Int {
        require(file < crcs.size) { "CRC file id out of bounds." }
        return crcs[file]
    }

    /**
     * Gets the amount of file CRCs.
     */
    val size: Int
        get() = crcs.size

    companion object {

        /**
         * The name suffix of CrcList entries.
         */
        internal const val ENTRY_NAME_SUFFIX = "_crc"
    }

}
