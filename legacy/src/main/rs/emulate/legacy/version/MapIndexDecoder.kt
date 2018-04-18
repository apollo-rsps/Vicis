package rs.emulate.legacy.version

import rs.emulate.legacy.archive.Archive
import rs.emulate.shared.util.DataBuffer

import java.io.FileNotFoundException

/**
 * Decoder for the version lists "map_index" entry.
 *
 * @param archive The version list [Archive].
 * @throws FileNotFoundException If the `map_index` entry could not be found.
 */
class MapIndexDecoder constructor(archive: Archive) {

    private val data: DataBuffer = archive.getEntry(MapIndex.ENTRY_NAME).buffer.asReadOnlyBuffer()

    /**
     * Decodes the contents of the `map_index` entry into a [MapIndex].
     */
    fun decode(): MapIndex {
        val count = data.remaining() / (3 * java.lang.Short.BYTES + java.lang.Byte.BYTES)

        val areas = IntArray(count)
        val maps = IntArray(count)
        val objects = IntArray(count)
        val members = BooleanArray(count)

        for (index in 0 until count) {
            areas[index] = data.getUnsignedShort()
            maps[index] = data.getUnsignedShort()
            objects[index] = data.getUnsignedShort()
            members[index] = data.getBoolean()
        }

        return MapIndex(areas, objects, maps, members)
    }

}
