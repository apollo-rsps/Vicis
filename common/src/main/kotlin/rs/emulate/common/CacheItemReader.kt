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
    fun readAll() = reader.listing(cache).map(this::read).asSequence()

    /**
     * Read a single item and return the decoded representation.
     */
    fun read(id: IdentityT): ItemT {
        try {
            val data = reader.load(cache, id)
            return decoder.decode(id, data)
        } catch (err: Exception) {
            throw Exception("Unable to decode item with id '$id'", err)
        }
    }
}
