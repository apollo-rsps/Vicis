package rs.emulate.modern.config

import rs.emulate.common.CacheDataReader
import rs.emulate.modern.Index
import rs.emulate.modern.ModernCache


class ModernConfigDataReader(
    private val cache: ModernCache,
    private val archiveEntryId: Int
) : CacheDataReader<Int> {

    private val archive by lazy {
        cache.openArchive(
            Index.CONFIG,
            archiveEntryId
        )
    }

    override fun load(id: Int) = archive.read(id)

    override fun listing() = archive.listEntries
}
