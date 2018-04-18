package rs.emulate.legacy.version

import com.google.common.collect.ImmutableList
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveEntry
import java.io.FileNotFoundException
import java.util.ArrayList
import java.util.Arrays

/**
 * Decodes [CrcList]s from the `versionlist` [Archive].
 *
 * @param version The [Archive] containing the version data.
 * @param types The [VersionEntryType]s to decode.
 */
class CrcListDecoder @JvmOverloads constructor(
    private val version: Archive,
    types: List<VersionEntryType> = StandardVersionEntryType.values().toList()
) {

    private val types: List<VersionEntryType> = types.toList()

    /**
     * Decodes the [CrcList]s.
     *
     * @throws FileNotFoundException If any of the [ArchiveEntries][ArchiveEntry] could not be found.
     */
    @Throws(FileNotFoundException::class)
    fun decode(): List<CrcList> {
        val lists = ArrayList<CrcList>(types.size)

        for (type in types) {
            val name = type.asCrcList()
            val entry = version.getEntry(name)
            val data = entry.buffer

            val count = data.limit() / Integer.BYTES
            val crcs = IntArray(count)
            Arrays.setAll(crcs) { data.getInt() }

            lists.add(CrcList(type, crcs))
        }

        return ImmutableList.copyOf(lists)
    }

}
