package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.`object`.DefaultObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.item.DefaultItemDefinition
import rs.emulate.legacy.config.item.ItemDefinition
import rs.emulate.legacy.config.npc.DefaultNpcDefinition
import rs.emulate.legacy.config.npc.NpcDefinition

class ObjectResourceBundle(config: Archive) : ConfigResourceBundle<ObjectResourceId, ObjectDefinition> {

    override val idType = ObjectResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { ObjectResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.entry {
                id = resourceId
                label = "${def.id} - ${def.name.value}"
                type = def.javaClass.simpleName
            }
        }
    }

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = ObjectDefinition.ENTRY_NAME,
            definition = ObjectDefinition::class.java,
            default = DefaultObjectDefinition::class.java
        )
    }

}

class NpcResourceBundle(config: Archive) : ConfigResourceBundle<NpcResourceId, NpcDefinition> {

    override val idType = NpcResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { NpcResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.entry {
                id = resourceId
                label = "${def.id} - ${def.name.value}"
                type = def.javaClass.simpleName
            }
        }
    }

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = NpcDefinition.ENTRY_NAME,
            definition = NpcDefinition::class.java,
            default = DefaultNpcDefinition::class.java
        )
    }

}

class ItemResourceBundle(config: Archive) : ConfigResourceBundle<ItemResourceId, ItemDefinition> {

    override val idType = ItemResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { ItemResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.entry {
                id = resourceId
                label = "${def.id} - ${def.name.value}"
                type = def.javaClass.simpleName
            }
        }
    }

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = ItemDefinition.ENTRY_NAME,
            definition = ItemDefinition::class.java,
            default = DefaultItemDefinition::class.java
        )
    }

}
