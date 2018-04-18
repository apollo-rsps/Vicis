package rs.emulate.modern.tools

import rs.emulate.modern.Container
import rs.emulate.modern.FileStore
import rs.emulate.modern.ReferenceTable

/**
 * A utility to defragment a cache.
 */
object CacheDefragmenter {

    @JvmStatic
    fun main(args: Array<String>) {
        FileStore.open("../game/data/cache/").use { input ->
            FileStore.create("/tmp/defragmented-cache", input.typeCount).use { out ->
                for (type in 0 until input.typeCount) {
                    val buffer = input.read(255, type)
                    buffer.mark()
                    out.write(255, type, buffer)
                    buffer.reset()

                    val table = ReferenceTable.decode(Container.decode(buffer).data)
                    for (file in 0 until table.capacity()) {
                        if (table.getEntry(file) == null) {
                            continue
                        }

                        out.write(type, file, input.read(type, file))
                    }
                }
            }
        }
    }

}
