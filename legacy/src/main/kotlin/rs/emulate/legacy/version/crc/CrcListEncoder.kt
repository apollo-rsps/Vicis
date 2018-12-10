package rs.emulate.legacy.version.crc

import io.netty.buffer.Unpooled
import rs.emulate.legacy.archive.ArchiveEntry

/**
 * Encodes [CrcList]s into [ArchiveEntry] objects.
 */
class CrcListEncoder(crcs: List<CrcList>) {

    private val lists: List<CrcList> = crcs.toList()

    fun encode(): Array<ArchiveEntry> {
        val size = lists.size
        val entries = arrayOfNulls<ArchiveEntry>(size)

        for (index in 0 until size) {
            val list = lists[index]
            val type = list.type
            val crcs = list.crcs

            val buffer = Unpooled.buffer(crcs.size * Integer.BYTES)
            crcs.forEach { buffer.writeInt(it) }

            entries[index] = ArchiveEntry(type.asCrcList(), buffer)
        }

        return entries.requireNoNulls()
    }

}
