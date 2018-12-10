package rs.emulate.legacy.config

/**
 * Contains constants related to entries in the config archive.
 */
object Config {

    /**
     * The extension for entries containing data.
     */
    const val DATA_EXTENSION = ".dat"

    /**
     * The extension for entries containing the data index.
     */
    const val INDEX_EXTENSION = ".idx"

    const val DEFINITION_TERMINATOR = 0

}

interface Definition {
    val id: Int
}
