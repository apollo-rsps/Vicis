package rs.emulate.legacy.version.map

/**
 * Contains data used for mapping region coordinates to object and map files.
 *
 * @param areas The area ids, of the form `region_x << 8 | region_y`.
 * @param objects The object file ids.
 * @param maps The map file ids.
 * @param members The members-only flags.
 */
class MapIndex(areas: IntArray, objects: IntArray, maps: IntArray, members: BooleanArray) {

    val areas: IntArray = areas.clone()
        get() = field.clone()

    val maps: IntArray = maps.clone()
        get() = field.clone()

    val members: BooleanArray = members.clone()
        get() = field.clone()

    val objects: IntArray = objects.clone()
        get() = field.clone()

    /**
     * The amount of elements in this MapIndex
     */
    val size: Int
        get() = areas.size

    companion object {

        /**
         * The name of the archive entry containing the map index data.
         */
        const val ENTRY_NAME = "map_index"

        /**
         * Creates a new MapIndex.
         *
         * @param areas The area ids, of the form `region_x << 8 | region_y`.
         * @param objects The object file ids.
         * @param maps The map file ids.
         * @param members The members-only flags.
         */
        fun create(areas: IntArray, objects: IntArray, maps: IntArray, members: BooleanArray): MapIndex {
            val expected = members.size
            require(expected == areas.size && expected == objects.size && expected == maps.size) {
                "MapIndex arrays must be of equal length."
            }

            return MapIndex(areas, objects, maps, members)
        }
    }

}
