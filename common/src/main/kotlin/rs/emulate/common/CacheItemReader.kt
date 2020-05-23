package rs.emulate.common

import io.netty.buffer.ByteBuf
import rs.emulate.common.config.Definition

class CacheItemReader<CacheT, IdentityT, ItemT : CacheItem<IdentityT>>(
    private val cache: CacheT,
    private val reader: CacheDataLoader<CacheT, IdentityT>,
    private val decoder: CacheItemDecoder<IdentityT, ItemT>
) {
    /**
     * Read all available config [Definition]s into a lazily evaluated sequence.
     */
    fun readAll() = reader.listing(cache).asSequence().map(this::read)

    /**
     * Read a single item and return the decoded representation.
     */
    fun read(id: IdentityT): ItemT {
        val data = reader.load(cache, id)
        val item = decoder.decode(id, data)

        return item
    }
}
