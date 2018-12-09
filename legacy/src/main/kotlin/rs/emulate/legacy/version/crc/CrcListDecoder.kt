package rs.emulate.legacy.version.crc

import com.google.common.collect.ImmutableList
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.version.StandardVersionEntryType
import rs.emulate.legacy.version.VersionEntryType
import java.util.ArrayList
import java.util.Arrays

/**
 * Decodes [CrcList]s from the `versionlist` [Archive].
 *
 * @param version The [Archive] containing the version data.
 * @param types The [VersionEntryType]s to decode.
 */
class CrcListDecoder(
    private val version: Archive,
    types: List<VersionEntryType> = StandardVersionEntryType.values().toList()
) {

    private val types: List<VersionEntryType> = types.toList()

    /**
     * Decodes the [CrcList]s.
     */
    fun decode(): List<CrcList> {
        val lists = ArrayList<CrcList>(types.size)

        for (type in types) {
            val name = type.asCrcList()
            val entry = version[name]
            val data = entry.buffer

            val count = data.limit() / Integer.BYTES
            val crcs = IntArray(count)
            Arrays.setAll(crcs) { data.getInt() }

            lists.add(CrcList(type, crcs))
        }

        return ImmutableList.copyOf(lists)
    }

}
