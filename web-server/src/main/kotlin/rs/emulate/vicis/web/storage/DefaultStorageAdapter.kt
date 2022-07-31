package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.*
import rs.emulate.vicis.web.storage.StorageCacheLookup.*

data class StorageWorkingCopy<IdT, EntityT>(val items: Map<IdT, EntityT>, val token: StorageCacheToken)

// TODO: fix not-null assertions.
abstract class DefaultStorageAdapter<IdT, EntityT> : StorageAdapter<IdT, EntityT> {
    abstract fun loadWorkingCopy(): Result<StorageWorkingCopy<IdT, EntityT>, StorageError>
    abstract fun storeWorkingCopy(workingCopy: StorageWorkingCopy<IdT, EntityT>): Result<Unit, StorageError>
    abstract fun getElementId(element: EntityT): IdT

    private var workingCopy: StorageWorkingCopy<IdT, EntityT>? = null

    private fun getOrCreateWorkingCopy(token: StorageCacheToken? = null): Result<StorageWorkingCopy<IdT, EntityT>, StorageError> {
        if (workingCopy == null || workingCopy?.token != token) {
            return loadWorkingCopy().onSuccess { newCopy -> workingCopy = newCopy }
        }

        return workingCopy?.let { Ok(it) } ?: Err(UnknownStorageError("Working copy was never initialized"))
    }

    override fun getAll(token: StorageCacheToken?): Result<StorageCacheLookup<List<EntityT>>, StorageError> =
        getOrCreateWorkingCopy(token)
            .map { workingCopy ->
                if (workingCopy.token == token) {
                    CacheHit()
                } else {
                    CacheMiss(workingCopy.items.values.toList(), workingCopy.token)
                }
            }

    override fun get(id: IdT, token: StorageCacheToken?) = getOrCreateWorkingCopy(token)
        .map { workingCopy ->
            if (workingCopy.token == token) {
                CacheHit()
            } else {
                CacheMiss<EntityT>(workingCopy.items[id]!!, workingCopy.token)
            }
        }

    override fun putAll(entities: List<EntityT>, token: StorageCacheToken) {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }
}