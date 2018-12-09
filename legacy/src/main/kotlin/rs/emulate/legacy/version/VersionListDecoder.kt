package rs.emulate.legacy.version

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveEntry
import rs.emulate.util.getUnsignedShort
import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.Arrays

/**
 * Decodes ArchiveEntry objects from the version [Archive] containing file version data.
 *
 * @param versions The [Archive] containing the version data.
 */
class VersionListDecoder(private val versions: Archive) {

    /**
     * Decodes the file [VersionList]s.
     * @throws FileNotFoundException If any of the [ArchiveEntry] names could not be found.
     */
    fun decode(): List<VersionList> {
        val lists = ArrayList<VersionList>(VersionList.VERSION_ENTRY_NAMES.size)

        for (type in VersionList.VERSION_ENTRY_NAMES.indices) {
            val name = VersionList.VERSION_ENTRY_NAMES[type]
            val entry = versions[name]
            val data = entry.buffer

            val count = data.limit() / java.lang.Short.BYTES
            val versions = IntArray(count)
            Arrays.setAll(versions) { data.getUnsignedShort() }

            lists.add(VersionList(versions))
        }

        return lists
    }

}
