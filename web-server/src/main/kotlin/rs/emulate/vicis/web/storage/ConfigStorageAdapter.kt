package rs.emulate.vicis.web

import com.github.michaelbull.result.Result
import rs.emulate.common.config.Definition
import java.time.ZonedDateTime


enum class ConfigStorageError {
    DataFileUnavailable,
    IndexFileUnavailable,
    MalformedContent,
}
typealias ConfigLoadResult<T> = Result<List<T>, ConfigStorageError>;

interface ConfigStorageAdapter<DefT : Definition> {

    fun loadAll(): ConfigLoadResult<DefT>;

    fun saveAll(items: List<DefT>)
}

