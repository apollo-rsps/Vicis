package rs.emulate.legacy.archive

/**
 * Contains archive-related utility methods.
 */
object ArchiveUtils {

    /**
     * Hashes the specified string into an integer used to identify an [ArchiveEntry].
     */
    fun hash(name: String): Int {
        return name.toUpperCase().chars().reduce(0) { hash, character -> hash * 61 + character - 32 }
    }

    /**
     * Gets the size of the data contained in the specified [ArchiveEntry] objects.
     */
    fun sizeOf(entries: List<ArchiveEntry>): Int {
        return entries.sumBy(ArchiveEntry::size)
    }

}
