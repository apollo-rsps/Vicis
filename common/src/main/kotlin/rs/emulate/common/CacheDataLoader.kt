package rs.emulate.common

import io.netty.buffer.ByteBuf

//interface CacheDataLoader<IdentityT> {
//
//    /**
//     * Load the data associated with the config item identified by [id].
//     */
//    fun load(id: IdentityT): ByteBuf
//
//    /**
//     * Create a listing of all config item identities with the type loaded by this reader.
//     */
//    fun listing(): List<IdentityT>
//}

interface CacheDataLoader<CacheT, IdentityT> {
    fun load(cache: CacheT, id: IdentityT): ByteBuf

    fun listing(cache: CacheT): Sequence<IdentityT>
}
