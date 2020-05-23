package rs.emulate.common.map

import rs.emulate.common.CacheItem

data class MapLocation(val id: Int, val coordinates: Int, val type: Int, val orientation: Int)

class MapLocations(override val id: MapId, val locations: MutableList<MapLocation>) : CacheItem<MapId> {
}
