package rs.emulate.common

import io.netty.buffer.ByteBuf

interface CacheDataReader<IdentityT> {

    /**
     * Load the data associated with the config item identified by [id].
     */
    fun load(id: IdentityT): ByteBuf

    /**
     * Create a listing of all config item identities with the type loaded by this reader.
     */
    fun listing(): List<IdentityT>
}
