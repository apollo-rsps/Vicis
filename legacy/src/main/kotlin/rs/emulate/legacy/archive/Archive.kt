package rs.emulate.legacy.archive

import java.io.FileNotFoundException

/**
 * An archive in the RuneScape cache. An archive is a set of files that can be completely compressed, or each
 * individual file can be compressed.
 */
class Archive(val entries: List<ArchiveEntry>) {

    val size: Int
        get() = entries.sumBy(ArchiveEntry::size)

    operator fun get(name: String): ArchiveEntry {
        return getOrNull(name) ?: throw FileNotFoundException("Missing entry $name")
    }

    fun getOrNull(name: String): ArchiveEntry? {
        val hash = name.entryHash()
        return entries.firstOrNull { it.identifier == hash }
    }

    override fun equals(other: Any?): Boolean {
        return entries == (other as? Archive)?.entries
    }

    override fun hashCode(): Int {
        return entries.hashCode()
    }

    companion object {

        /**
         * Hashes an [ArchiveEntry] name into an integer to be used as an identifier.
         */
        fun String.entryHash(): Int {
            return toUpperCase().fold(0) { hash, character -> hash * 61 + character.toInt() - 32 }
        }

        val EMPTY_ARCHIVE = Archive(emptyList())
    }

}
