package rs.emulate.common

import io.netty.buffer.ByteBuf
import rs.emulate.common.config.Definition

class CacheItemReader<IdentityT, ItemT: CacheItem<IdentityT>>(
    private val reader: CacheDataReader<IdentityT>,
    private val decoder: (IdentityT, ByteBuf) -> ItemT
) {
    /**
     * Read all available config [Definition]s into a lazily evaluated sequence.
     */
    fun readAll() = reader.listing().asSequence().map(this::read)

    /**
     * Read a single item and return the decoded representation.
     */
    fun read(id: IdentityT): ItemT {
        val data = reader.load(id)
        val item = decoder(id, data)

        return item
    }
}
