package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.legacy.IndexedFileSystem

class ConfigResourceBundleFactory(fs: IndexedFileSystem) {

    private val config = fs.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)

    val items = ItemResourceBundle(config)
    val npcs = NpcResourceBundle(config)
    val objects = ObjectResourceBundle(config)

    fun bundles(): List<ResourceBundle<*>> = listOf(items, npcs, objects)

    private companion object {
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }
}
