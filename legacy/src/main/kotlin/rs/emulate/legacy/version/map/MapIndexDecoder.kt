package rs.emulate.legacy.version.map

import rs.emulate.legacy.archive.Archive
import java.io.FileNotFoundException

/**
 * Decoder for the version lists "map_index" entry.
 *
 * @param archive The version list [Archive].
 * @throws FileNotFoundException If the `map_index` entry could not be found.
 */
class MapIndexDecoder constructor(archive: Archive) {

    private val data = archive[MapIndex.ENTRY_NAME].buffer.copy()

    /**
     * Decodes the contents of the `map_index` entry into a [MapIndex].
     */
    fun decode(): MapIndex {
        val count = data.readableBytes() / (3 * java.lang.Short.BYTES + java.lang.Byte.BYTES)

        val areas = IntArray(count)
        val maps = IntArray(count)
        val objects = IntArray(count)
        val members = BooleanArray(count)

        for (index in 0 until count) {
            areas[index] = data.readUnsignedShort()
            maps[index] = data.readUnsignedShort()
            objects[index] = data.readUnsignedShort()
            members[index] = data.readBoolean()
        }

        return MapIndex(areas, objects, maps, members)
    }

}
