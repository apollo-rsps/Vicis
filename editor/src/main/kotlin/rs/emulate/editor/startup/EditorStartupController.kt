package rs.emulate.editor.startup

import io.reactivex.subjects.BehaviorSubject
import rs.emulate.editor.startup.EditorCacheType.*
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.legacy.LegacyResourceBundleLoader
import rs.emulate.editor.workspace.resource.modern.ModernResourceBundleLoader
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.*
import java.nio.file.Files
import java.nio.file.Path

class EditorStartupController : Controller() {
    /**
     * Event emitter for [load] events after an [IndexedFileSystem] has been created.
     */
    val onCacheLoad = BehaviorSubject.create<ResourceCache>()

    /**
     * Event emitter for [load] events when an attempt to load a cache has resulted in an error.
     */
    val onCacheLoadError = BehaviorSubject.create<Throwable>()

    /**
     * Load the filesystem from the cache location given by [path] and notify
     * the [onCacheLoad] emitter.
     */
    fun load(type: EditorCacheType, path: Path) {
        val cachePath = if (Files.isDirectory(path)) path else path.parent
        val bundleLoader = when(type) {
            Modern -> ModernResourceBundleLoader()
            Legacy -> LegacyResourceBundleLoader(cachePath)
        }

        val job = runAsync {
            val list = mutableListOf<ResourceBundle>()
            bundleLoader.load(list)

            ResourceCache(list)
        }

        job.success(onCacheLoad::onNext)
        job.fail(onCacheLoadError::onNext)
    }
}
