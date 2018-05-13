package rs.emulate.editor.ui.startup

import io.reactivex.subjects.BehaviorSubject
import rs.emulate.editor.ui.startup.EditorCacheType.Legacy
import rs.emulate.editor.ui.startup.EditorCacheType.Modern
import rs.emulate.editor.resource.ResourceBundle
import rs.emulate.editor.resource.ResourceCache
import rs.emulate.editor.resource.bundles.legacy.LegacyResourceBundleLoader
import rs.emulate.editor.resource.bundles.modern.ModernResourceBundleLoader
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.Controller
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
    fun load(type: EditorCacheType, path: Path): ResourceCache {
        val cachePath = if (Files.isDirectory(path)) path else path.parent
        val bundleLoader = when (type) {
            Modern -> ModernResourceBundleLoader()
            Legacy -> LegacyResourceBundleLoader(cachePath)
        }

        val list = mutableListOf<ResourceBundle<*>>()
        bundleLoader.load(list)

        return ResourceCache(list)
    }
}
