package rs.emulate.legacy.archive

import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.Arrays

/**
 * An archive in the RuneScape cache. An archive is a set of files which can be completely compressed, or each
 * individual file can be compressed. This implementation is immutable (i.e. calling any of the
 * `add/replace/remove` methods will return a new archive with the specified modification performed on it).
 *
 * @param entries The entries in this archive.
 */
class Archive(entries: List<ArchiveEntry>) {

    /**
     * The entries in this archive.
     */
    val entries: List<ArchiveEntry> = entries.toList()
        get() = field.toList()

    /**
     * The size of this archive.
     */
    val size: Int = ArchiveUtils.sizeOf(entries)

    /**
     * Returns a new archive consisting of the current [ArchiveEntry] objects, and the specified entries. This
     * will replace any entries with the same identifier as any of the specified ones, if present.
     */
    fun addEntries(vararg entries: ArchiveEntry): Archive {
        val current = ArrayList(this.entries)
        val additions = Arrays.asList(*entries)

        current.removeIf { entry -> additions.stream().anyMatch { e -> e.identifier == entry.identifier } }
        current.addAll(additions)

        return Archive(current)
    }

    /**
     * Returns a new archive consisting of the current [ArchiveEntry] objects, and the added one. This will
     * replace the entry with the same identifier as the specified one, if it exists.
     */
    fun addEntry(entry: ArchiveEntry): Archive {
        return addEntries(entry)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Archive) {
            return size == other.size && entries == other.entries
        }

        return false
    }

    /**
     * Gets the [ArchiveEntry] with the specified name.
     * @throws FileNotFoundException If the entry could not be found.
     */
    @Throws(FileNotFoundException::class)
    fun getEntry(name: String): ArchiveEntry {
        val hash = ArchiveUtils.hash(name)
        val optional = entries.stream().filter { entry -> entry.identifier == hash }.findFirst()

        return optional.orElseThrow { FileNotFoundException("Could not find entry: $name.") }
    }

    /**
     * Gets the [ArchiveEntry] with the specified hash.
     *
     * @param hash The hash of the entry.
     * @return The entry.
     * @throws FileNotFoundException If the entry could not be found.
     */
    @Throws(FileNotFoundException::class)
    fun getEntry(hash: Int): ArchiveEntry {
        val optional = entries.stream().filter { entry -> entry.identifier == hash }.findFirst()

        return optional.orElseThrow { FileNotFoundException("Could not find entry: $hash.") }
    }

    override fun hashCode(): Int {
        return entries.hashCode()
    }

    /**
     * Removes the [ArchiveEntry] with the specified name.
     *
     * @param name The name of the entry.
     * @return An archive containing all of the entries in this archive, except for the one removed.
     * @throws FileNotFoundException If the entry could not be found.
     */
    @Throws(FileNotFoundException::class)
    fun removeEntry(name: String): Archive {
        val hash = ArchiveUtils.hash(name)
        val entries = ArrayList(this.entries)

        val iterator = entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.identifier == hash) {
                iterator.remove()
                return Archive(entries)
            }
        }

        throw FileNotFoundException("Could not find entry: $name.")
    }

    /**
     * Replaces the [ArchiveEntry] with the specified name (shorthand for [removeEntry] and then [addEntry].
     *
     * @param name The name of the entry to replace.
     * @param entry The entry.
     * @return The archive with the replaced entry.
     * @throws FileNotFoundException If the entry with the specified name is not part of this archive.
     */
    @Throws(FileNotFoundException::class)
    fun replaceEntry(name: String, entry: ArchiveEntry): Archive {
        return removeEntry(name).addEntry(entry) // TODO rewrite to avoid needless copying
    }

    companion object {

        /**
         * The empty Archive.
         */
        val EMPTY_ARCHIVE = Archive(emptyList())
    }

}
