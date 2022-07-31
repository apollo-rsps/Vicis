package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveCodec

fun <T> runCatchingStorageError(block: () -> T) = runCatching(block).mapError { UnknownStorageError(it.localizedMessage) }

fun IndexedFileSystem.findFile(type: Int, id: Int) = runCatchingStorageError { get(type, id) }

fun IndexedFileSystem.findArchive(type: Int, id: Int) = runCatching { get(type, id) }
    .mapError { CorruptedOrMissingIndex(it.localizedMessage) }
    .andThen { buffer ->
        runCatching { ArchiveCodec.decode(buffer) }
            .mapError { UnknownStorageError(it.localizedMessage) }
    }

fun Archive.getFile(name: String) = runCatching { get(name) }
    .mapError { MissingFile(name) }