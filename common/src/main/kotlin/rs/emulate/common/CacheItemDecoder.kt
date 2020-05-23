package rs.emulate.common

import io.netty.buffer.ByteBuf

interface CacheItemDecoder<IdentityT, ItemT : CacheItem<IdentityT>> {
    fun decode(id: IdentityT, input: ByteBuf): ItemT
}
