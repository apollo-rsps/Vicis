package rs.emulate.legacy

/**
 * A mode to open an [IndexedFileSystem] with.
 *
 * @param unix The UNIX-style file system permission string of this access mode.
 */
enum class AccessMode(private val unix: String) {

    /**
     * The read-only access mode.
     */
    READ("r"),

    /**
     * The read-write access mode.
     */
    READ_WRITE("rw"),

    /**
     * The write-only access mode.
     */
    WRITE("w");

    /**
     * Returns the UNIX-style file system permission string of this access mode.
     */
    fun asUnix(): String {
        return unix
    }

}
