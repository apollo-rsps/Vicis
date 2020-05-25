package rs.emulate.cache.osrs

import rs.emulate.cache.CacheBuilder
import rs.emulate.common.config.location.LocationDefinitionDecoder
import rs.emulate.common.config.npc.NpcDefinitionDecoder
import rs.emulate.common.config.obj.ObjectDefinitionDecoder
import rs.emulate.common.map.MapFileDecoder
import rs.emulate.common.map.MapId
import rs.emulate.common.map.MapLocationsDecoder
import rs.emulate.modern.Index
import rs.emulate.modern.ModernCache
import rs.emulate.modern.codec.store.FileStoreOption
import rs.emulate.modern.loader.ConfigLoader
import rs.emulate.modern.loader.MapFileKind
import rs.emulate.modern.loader.MapLoader
import rs.emulate.util.crypto.xtea.XteaKey
import java.nio.file.Path

class OsrsCacheBuilder : CacheBuilder<ModernCache>() {

    val mapKeys = mutableMapOf<MapId, XteaKey>()

    var source: Path? = null

    init {
        configure(ConfigLoader(ITEM_CONFIG), ObjectDefinitionDecoder)
        configure(ConfigLoader(NPC_CONFIG), NpcDefinitionDecoder)
        configure(ConfigLoader(LOC_CONFIG), LocationDefinitionDecoder)
        configure(MapLoader(MapFileKind.Terrain), MapFileDecoder)
        configure(MapLoader(MapFileKind.Locations, mapKeys), MapLocationsDecoder)
    }

    override fun build(): ModernCache {
        val source = requireNotNull(source) { "A path to a cache must be provided" }

        return ModernCache.open(source, FileStoreOption.Lenient)
    }

    companion object {
        const val LOC_CONFIG = 6
        const val NPC_CONFIG = 9
        const val ITEM_CONFIG = 10
    }
}

