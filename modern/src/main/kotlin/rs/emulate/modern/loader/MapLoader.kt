package rs.emulate.modern.loader

import io.netty.buffer.ByteBuf
import rs.emulate.common.CacheDataLoader
import rs.emulate.common.map.MapId
import rs.emulate.modern.Index
import rs.emulate.modern.ModernCache
import rs.emulate.util.crypto.xtea.XteaKey

enum class MapFileKind(val prefix: Char) {
    Locations('l'),
    Terrain('m')
}

class MapLoader(
    private val kind: MapFileKind,
    private val keys: Map<MapId, XteaKey>? = null
) : CacheDataLoader<ModernCache, MapId> {

    private fun mapFileName(x: Int, y: Int) ="${kind.prefix}${x}_${y}"

    override fun load(cache: ModernCache, id: MapId): ByteBuf {
        val key = keys?.get(id) ?: XteaKey.NONE
        val archive = cache.openArchive(Index.MAPS, mapFileName(id.x, id.y), key)

        return archive.read(0)
    }

    override fun listing(cache: ModernCache): Sequence<MapId> {
        val items = mutableListOf<MapId>()

        repeat(256) { x ->
            repeat(100) { y ->
                val name = mapFileName(x, y)
                val mapId = MapId(x, y)
                val hasKeyIfRequired = keys == null || keys.get(mapId) != null

                if (cache.contains(Index.MAPS, name) && hasKeyIfRequired) {
                    items += mapId
                }
            }
        }

        return items.asSequence()
    }
}
