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
    private val keys: Map<MapId, XteaKey> = emptyMap()
) : CacheDataLoader<ModernCache, MapId> {

    private fun mapFileName(x: Int, y: Int) ="${kind.prefix}${x}_${y}"

    override fun load(cache: ModernCache, id: MapId): ByteBuf {
        val key = keys[id] ?: XteaKey.NONE

        return cache.read(Index.MAPS, mapFileName(id.x, id.y), key)
    }

    override fun listing(cache: ModernCache): Sequence<MapId> {
        val items = mutableListOf<MapId>()

        repeat(256) { x ->
            repeat(100) { y ->
                val name = mapFileName(x, y)

                if (cache.contains(Index.MAPS, name)) {
                    items += MapId(x, y)
                }
            }
        }

        return items.asSequence()
    }
}
