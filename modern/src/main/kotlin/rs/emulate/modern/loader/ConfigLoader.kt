package rs.emulate.modern.loader

import rs.emulate.common.CacheDataLoader
import rs.emulate.modern.Index
import rs.emulate.modern.ModernCache
import rs.emulate.modern.codec.Archive
import rs.emulate.modern.codec.CacheArchive
import java.util.*

class ConfigLoader(
    private val archiveEntryId: Int
) : CacheDataLoader<ModernCache, Int> {

    private val cachedArchives = WeakHashMap<ModernCache, CacheArchive>()

    private fun archive(cache: ModernCache) = cachedArchives.computeIfAbsent(cache) {
        cache.openArchive(
            Index.CONFIG,
            archiveEntryId
        )
    }

    override fun load(cache: ModernCache, id: Int) = archive(cache).read(id)

    override fun listing(cache: ModernCache) = archive(cache).listEntries.asSequence()
}
