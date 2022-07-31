package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.Result

sealed class StorageError {

}

data class CorruptedOrMissingIndex(val message: String) : StorageError()
data class UnknownStorageError(val message: String) : StorageError()
data class MissingFile(val filename: String) : StorageError()

sealed class StorageCacheLookup<T> {
    class CacheHit<T> : StorageCacheLookup<T>()
    data class CacheMiss<T>(val value: T, val token: StorageCacheToken) : StorageCacheLookup<T>()

}

interface StorageAdapter<IdT, EntityT> : AutoCloseable {
    fun getAll(token: StorageCacheToken? = null): Result<StorageCacheLookup<List<EntityT>>, StorageError>
    fun get(id: IdT, token: StorageCacheToken? = null): Result<StorageCacheLookup<EntityT>, StorageError>
    fun putAll(entities: List<EntityT>, token: StorageCacheToken)
    fun flush()

    override fun close() {
        flush()
    }
}