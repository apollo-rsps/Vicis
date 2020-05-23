package rs.emulate.cache

import rs.emulate.common.CacheDataLoader
import rs.emulate.common.CacheItem
import rs.emulate.common.CacheItemDecoder
import rs.emulate.common.CacheItemReader
import java.io.Closeable
import kotlin.reflect.KClass

private typealias LoaderImplementation<CacheT> = CacheDataLoader<CacheT, Any>
private typealias ReaderEntry<CacheT> = Pair<LoaderImplementation<CacheT>, CacheItemDecoder<Any, CacheItem<Any>>>

abstract class CacheBuilder<CacheT : Closeable> {
    private val readerImplementations = mutableMapOf<KClass<out CacheItem<*>>, ReaderEntry<CacheT>>()

    fun load(): Cache<CacheT> {
        val cache = build()
        val readers = mutableMapOf<KClass<out CacheItem<*>>, CacheItemReader<CacheT, *, *>>()

        for ((type, entry) in readerImplementations) {
            readers[type] = CacheItemReader(cache, entry.first, entry.second)
        }

        return Cache(cache, readers.toMap())
    }

    fun <IdentityT, ItemT : CacheItem<IdentityT>> configure(
        type: KClass<ItemT>,
        loader: CacheDataLoader<CacheT, IdentityT>,
        decoder: CacheItemDecoder<IdentityT, ItemT>
    ) {
        readerImplementations[type] = (loader to decoder) as ReaderEntry<CacheT>
    }

    inline fun <IdentityT, reified ItemT : CacheItem<IdentityT>> configure(
        loader: CacheDataLoader<CacheT, IdentityT>,
        decoder: CacheItemDecoder<IdentityT, ItemT>
    ) = configure(ItemT::class, loader, decoder)

    protected abstract fun build(): CacheT
}
