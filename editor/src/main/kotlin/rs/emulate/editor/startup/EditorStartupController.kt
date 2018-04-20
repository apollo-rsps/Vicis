package rs.emulate.editor.startup

import io.reactivex.subjects.BehaviorSubject
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.*
import java.nio.file.Files
import java.nio.file.Path

class EditorStartupController : Controller() {
    /**
     * Event emitter for [load] events after an [IndexedFileSystem] has been created.
     */
    val onCacheLoaded = BehaviorSubject.create<IndexedFileSystem>()

    /**
     * Load the filesystem from the cache location given by [cachePath] and notify
     * the [onCacheLoaded] emitter.
     */
    fun load(cachePath: Path) {
        val job = runAsync {
            IndexedFileSystem(if (Files.isDirectory(cachePath)) cachePath else cachePath.parent, AccessMode.READ)
        }

        job.success(onCacheLoaded::onNext)
        job.fail(onCacheLoaded::onError)
    }
}
