package rs.emulate.legacy.version

import rs.emulate.legacy.archive.ArchiveEntry
import rs.emulate.shared.util.DataBuffer

import java.util.Arrays

/**
 * Encodes [CrcList]s into [ArchiveEntry] objects.
 */
class CrcListEncoder(crcs: List<CrcList>) {

    /**
     * The List of CrcLists.
     */
    private val lists: List<CrcList> = crcs.toList()

    /**
     * Encodes the [CrcList]s.
     *
     * @return The array of [ArchiveEntry] objects.
     */
    fun encode(): Array<ArchiveEntry> {
        val size = lists.size
        val entries = arrayOfNulls<ArchiveEntry>(size)

        for (index in 0 until size) {
            val list = lists[index]
            val type = list.type
            val crcs = list.crcs

            val buffer = DataBuffer.allocate(crcs.size * Integer.BYTES)
            Arrays.stream(crcs).forEach({ buffer.putInt(it) })

            entries[index] = ArchiveEntry(type.asCrcList(), buffer)
        }

        return entries.requireNoNulls()
    }

}
