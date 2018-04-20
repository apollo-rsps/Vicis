package rs.emulate.legacy.version

/**
 * A list of file versions, for a particular ArchiveEntry (such as models).
 *
 * @param versions The file versions. Changes to this array will not affect this list.
 */
class VersionList(versions: IntArray) {

    /**
     * The file versions.
     */
    val versions: IntArray = versions.clone()
        get() = field.clone()

    /**
     * The amount of file versions.
     */
    val size: Int
        get() = versions.size

    /**
     * Gets the version number of the specified file.
     */
    operator fun get(file: Int): Int {
        require(file < versions.size) { "Version file id out of bounds." }
        return versions[file]
    }

    companion object {

        /**
         * The names of ArchiveEntries containing file version data.
         */
        val VERSION_ENTRY_NAMES = arrayOf("model_version", "anim_version", "midi_version", "map_version")

        /**
         * The name suffix of VersionList entries.
         */
        internal const val ENTRY_NAME_SUFFIX = "_version"
    }

}
