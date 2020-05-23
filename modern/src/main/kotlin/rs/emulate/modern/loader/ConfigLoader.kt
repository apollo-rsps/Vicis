package rs.emulate.modern.loader

import rs.emulate.common.CacheDataLoader
import rs.emulate.modern.Index
import rs.emulate.modern.ModernCache

class ConfigLoader(
    private val archiveEntryId: Int
) : CacheDataLoader<ModernCache, Int> {

    private fun archive(cache: ModernCache) = cache.openArchive(
        Index.CONFIG,
        archiveEntryId
    )

    override fun load(cache: ModernCache, id: Int) = archive(cache).read(id)

    override fun listing(cache: ModernCache) = archive(cache).listEntries.asSequence()
}
