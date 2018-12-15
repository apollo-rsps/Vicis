package rs.emulate.editor.resource.bundles.legacy.config

//import org.controlsfx.control.PropertySheet
//import rs.emulate.editor.resource.Resource
//import rs.emulate.editor.resource.ResourceBundle
//import rs.emulate.editor.resource.bundles.legacy.config.npc.NpcResourceBundle
//import rs.emulate.editor.ui.workspace.components.propertysheet.PropertyEditorFactory
//import rs.emulate.editor.ui.workspace.components.propertysheet.PropertyItemFactory
//import rs.emulate.legacy.IndexedFileSystem
//
///**
// * A [Resource] from the config archive.
// */
//abstract class ConfigResource<T> : Resource {
//
//    abstract val definition: T
//    abstract val editorFactory: PropertyEditorFactory // TODO move somewhere else
//    protected abstract val generator: PropertyItemFactory<T> // TODO move somewhere else
//
//    fun items(): List<PropertySheet.Item> {
//        return generator.generate(this)
//    }
//
//    companion object {
//        const val INDEX_CATEGORY_NAME = "Config"
//    }
//
//}
//
//interface ConfigResourceBundle<I : ConfigResourceId, T> : ResourceBundle<I> {
//    val definitions: Map<I, T>
//}
//
//class ConfigResourceBundleFactory(fs: IndexedFileSystem) {
//
//    private val config = fs.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)
//    private val npcs = NpcResourceBundle(config)
//
//    fun bundles(): List<ResourceBundle<*>> = listOf(npcs)
//
//    private companion object {
//        const val CONFIG_INDEX = 0
//        const val CONFIG_ARCHIVE_ID = 2
//    }
//}
//
