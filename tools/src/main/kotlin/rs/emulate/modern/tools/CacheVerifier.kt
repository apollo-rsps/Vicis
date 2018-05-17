package rs.emulate.modern.tools

import rs.emulate.modern.Container
import rs.emulate.modern.FileStore
import rs.emulate.modern.ReferenceTable
import rs.emulate.shared.util.DataBuffer
import java.io.IOException
import java.util.zip.CRC32

/**
 * A utility to verify an existing cache is complete.
 */
object CacheVerifier {

    /**
     * The entry point for the application.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        FileStore.open("../game/data/cache/").use { store ->
            for (type in 0 until store.getFileCount(255)) {
                val table = ReferenceTable.decode(Container.decode(store.read(255, type)))
                for (file in 0 until table.capacity()) {
                    val entry = table.getEntry(file) ?: continue

                    val buffer: DataBuffer
                    try {
                        buffer = store.read(type, file)
                    } catch (ex: IOException) {
                        println(type.toString() + ":" + file + " error")
                        continue
                    }

                    if (buffer.capacity() <= 2) {
                        println(type.toString() + ":" + file + " missing")
                        continue
                    }

                    val bytes = ByteArray(buffer.limit() - 2)
                    buffer.position(0) // last two bytes are the version and shouldn't
                    buffer.read(bytes) // be included

                    val crc = CRC32()
                    crc.update(bytes, 0, bytes.size)

                    if (crc.value != entry.crc.toLong()) {
                        println(type.toString() + ":" + file + " corrupt")
                    }

                    buffer.position(buffer.limit() - 2)
                    if (buffer.getUnsignedShort() != entry.version and 0xFFFF) {
                        println(type.toString() + ":" + file + " out of date")
                    }
                }
            }
        }
    }

}
