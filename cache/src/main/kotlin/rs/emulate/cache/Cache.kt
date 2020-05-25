package rs.emulate.cache

import rs.emulate.cache.osrs.OsrsCacheBuilder
import rs.emulate.common.CacheItem
import rs.emulate.common.CacheItemReader
import java.io.Closeable
import java.nio.file.Paths
import kotlin.reflect.KClass

class Cache<CacheT : Closeable>(
    private val fs: CacheT,
    private val readers: Map<KClass<out CacheItem<*>>, CacheItemReader<CacheT, *, *>>
) : AutoCloseable by fs {

    private fun <IdentityT, ItemT : CacheItem<IdentityT>> getReader(type: KClass<ItemT>): CacheItemReader<CacheT, IdentityT, ItemT> {
        @Suppress("UNCHECKED_CAST") val reader = readers[type] as? CacheItemReader<CacheT, IdentityT, ItemT>
        if (reader == null) {
            error("No reader was found for cache items of type '$type'")
        }

        return reader
    }

    /**
     * Unwrap this [Cache] and return the underlying filesystem implementation.
     */
    fun unwrap() : CacheT = fs

    fun <IdentityT, ItemT: CacheItem<IdentityT>> read(id: IdentityT, type: Class<ItemT>): ItemT {
        return read(id, type.kotlin)
    }

    fun <IdentityT, ItemT : CacheItem<IdentityT>> read(id: IdentityT, type: KClass<ItemT>): ItemT {
        return getReader(type).read(id)
    }

    fun <IdentityT, ItemT : CacheItem<IdentityT>> readAll(type: Class<ItemT>): Sequence<ItemT> {
        return readAll(type.kotlin)
    }

    fun <IdentityT, ItemT : CacheItem<IdentityT>> readAll(type: KClass<ItemT>): Sequence<ItemT> {
        return getReader(type).readAll()
    }

    companion object {
        @JvmStatic
        fun osrs() = OsrsCacheBuilder()
    }
}
