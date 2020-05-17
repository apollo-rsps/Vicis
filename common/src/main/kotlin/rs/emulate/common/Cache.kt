package rs.emulate.common

import io.netty.buffer.ByteBuf
import rs.emulate.common.config.floor.FloorDefinitionDecoder
import rs.emulate.common.config.kit.IdentikitDefinitionDecoder
import rs.emulate.common.config.location.LocationDefinitionDecoder
import rs.emulate.common.config.npc.NpcDefinitionDecoder
import rs.emulate.common.config.obj.ObjectDefinitionDecoder
import kotlin.reflect.KClass

typealias CacheItemDecoder<IdentityT, ItemT> = (IdentityT, ByteBuf) -> ItemT

abstract class Cache {
    protected abstract fun <IdentityT> createDataReader(ty: KClass<out CacheItem<IdentityT>>): CacheDataReader<IdentityT>
    protected val decoders = mutableMapOf<KClass<out CacheItem<*>>, ((Any, ByteBuf) -> CacheItem<*>)>()

    init {
        addDecoder(ObjectDefinitionDecoder::decode)
        addDecoder(LocationDefinitionDecoder::decode)
        addDecoder(FloorDefinitionDecoder::decode)
        addDecoder(IdentikitDefinitionDecoder::decode)
        addDecoder(NpcDefinitionDecoder::decode)
    }

    protected inline fun <IdentityT, reified ItemT : CacheItem<IdentityT>> addDecoder(noinline decoder: (IdentityT, ByteBuf) -> ItemT) {
        @Suppress("UNCHECKED_CAST")
        decoders[ItemT::class] = decoder as (Any, ByteBuf) -> CacheItem<*>
    }

    // @TODO: Potentially nice API, horrible infrastructure. Refine this.
    fun <IdentityT, ItemT : CacheItem<IdentityT>> createReader(ty: KClass<ItemT>): CacheItemReader<IdentityT, ItemT> {
        val decoder = decoders[ty] ?: error("Unable to find a decoder for $ty")

        @Suppress("UNCHECKED_CAST")
        return CacheItemReader(createDataReader(ty), decoder as CacheItemDecoder<IdentityT, ItemT>)
    }

    inline fun <IdentityT, reified T : CacheItem<IdentityT>> createReader() = createReader(T::class)
}
