package rs.emulate.common.map

import rs.emulate.common.CacheItem

data class MapLocation(val id: Int, val coordinates: Int, val type: Int, val orientation: Int) {
    /**
     * Get the plane this map object exists on.
     *
     * @return The plane this map object is on.
     */
    val height: Int
        get() {
            return coordinates shr 12 and 0x3
        }

    /**
     * Get the X coordinate of this object relative to the map position.
     *
     * @return The local X coordinate.
     */
    val localX: Int
        get() {
            return coordinates shr 6 and 0x3F
        }

    /**
     * Get the Y coordinate of this object relative to the map position.
     *
     * @return The local Y coordinate.
     */
    val localY: Int
        get() {
            return coordinates and 0x3F
        }
}

class MapLocations(override val id: MapId, val locations: MutableList<MapLocation>) : CacheItem<MapId> {
}
