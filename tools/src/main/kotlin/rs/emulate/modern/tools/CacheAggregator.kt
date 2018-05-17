package rs.emulate.modern.tools

import rs.emulate.modern.Container
import rs.emulate.modern.Entry
import rs.emulate.modern.FileStore
import rs.emulate.modern.ReferenceTable
import rs.emulate.shared.util.DataBuffer

import java.io.IOException
import java.util.zip.CRC32

/**
 * A utility to aggregate multiple caches into one, to fill missing entries.
 */
object CacheAggregator {

    /**
     * The entry point for the application.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val otherStore = FileStore.open("/rscd/data/")
        val store = FileStore.open("../game/data/cache/")

        for (type in 0 until store.getFileCount(255)) {
            if (type == 7) {
                continue // TODO need support for newer ref table format for this index
            }

            val otherTable = ReferenceTable.decode(Container.decode(otherStore.read(255, type)))
            val table = ReferenceTable.decode(Container.decode(store.read(255, type)))

            for (file in 0 until table.capacity()) {
                val entry = table.getEntry(file) ?: continue

                if (isRepackingRequired(store, entry, type, file)) {
                    val otherEntry = otherTable.getEntry(file)!!
                    if (entry.version == otherEntry.version && entry.crc == otherEntry.crc) {
                        store.write(type, file, otherStore.read(type, file))
                    }
                }
            }
        }
    }

    /**
     * Returns whether or not cache repacking is required.
     */
    private fun isRepackingRequired(store: FileStore, entry: Entry, type: Int, file: Int): Boolean {
        val buffer: DataBuffer
        try {
            buffer = store.read(type, file)
        } catch (ex: IOException) {
            return true
        }

        if (buffer.capacity() <= 2) {
            return true
        }

        val bytes = ByteArray(buffer.limit() - 2) // last two bytes are the version and shouldn't be included
        buffer.position(0)
        buffer.read(bytes)

        val crc = CRC32()
        crc.update(bytes, 0, bytes.size)

        if (crc.value.toInt() != entry.crc) {
            return true
        }

        buffer.position(buffer.limit() - 2)
        return buffer.getUnsignedShort() == entry.version
    }

}
