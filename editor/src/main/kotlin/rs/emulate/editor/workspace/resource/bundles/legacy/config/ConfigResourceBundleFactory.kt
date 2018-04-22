package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.bundles.legacy.config.ConfigResourceBundleDelegate.Companion.bundle
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.`object`.DefaultObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.item.DefaultItemDefinition
import rs.emulate.legacy.config.item.ItemDefinition
import rs.emulate.legacy.config.npc.DefaultNpcDefinition
import rs.emulate.legacy.config.npc.NpcDefinition
import kotlin.reflect.KProperty

class ConfigResourceBundleFactory(fs: IndexedFileSystem) {

    private val config = fs.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)

    val objects by bundle(config, objectSupplier) {
        ObjectResourceId(id, name().value)
    }

    val items by bundle(config, itemSupplier) {
        ItemResourceId(id, name.value)
    }

    val npcs by bundle(config, npcSupplier) {
        NpcResourceId(id, name().value)
    }

    private companion object {
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2

        private val itemSupplier = DefinitionSupplier.create(
            ItemDefinition.ENTRY_NAME, ItemDefinition::class.java, DefaultItemDefinition::class.java
        )

        private val npcSupplier = DefinitionSupplier.create(
            NpcDefinition.ENTRY_NAME, NpcDefinition::class.java, DefaultNpcDefinition::class.java
        )

        private val objectSupplier = DefinitionSupplier.create(
            ObjectDefinition.ENTRY_NAME, ObjectDefinition::class.java, DefaultObjectDefinition::class.java
        )
    }

}

/**
 * A delegate that creates [ConfigResourceBundle]s.
 */
class ConfigResourceBundleDelegate<T : MutableConfigDefinition>(
    config: Archive,
    supplier: DefinitionSupplier<T>,
    toResourceId: T.() -> ResourceId
) {

    private val bundle: ConfigResourceBundle<T>

    init {
        val definitions = ConfigDecoder(config, supplier).decode().associateBy(toResourceId)
        bundle = ConfigResourceBundle(definitions, toResourceId)
    }

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): ConfigResourceBundle<T> = bundle

    companion object {

        fun <T : MutableConfigDefinition> bundle(
            config: Archive,
            supplier: DefinitionSupplier<T>,
            toResourceId: T.() -> ResourceId
        ): ConfigResourceBundleDelegate<T> = ConfigResourceBundleDelegate(
            config, supplier, toResourceId)

    }

}
