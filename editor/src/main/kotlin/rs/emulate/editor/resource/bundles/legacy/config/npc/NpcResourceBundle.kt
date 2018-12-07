package rs.emulate.editor.resource.bundles.legacy.config.npc

import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource
import rs.emulate.editor.resource.bundles.legacy.config.ConfigResourceBundle
import rs.emulate.editor.resource.bundles.legacy.config.NpcResourceId
import rs.emulate.editor.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigArchiveDecoder
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.config.npc.NpcDefinitionDecoder

class NpcResourceBundle(config: Archive) : ConfigResourceBundle<NpcResourceId, NpcDefinition> {

    override val idType = NpcResourceId::class

    override val definitions = ConfigArchiveDecoder.decode(config, NpcDefinitionDecoder)
        .associateBy { NpcResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(
                ConfigResource.INDEX_CATEGORY_NAME) {
                category("Npc Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id} - ${def.name}"
                    }
                }
            }
        }
    }

    override fun load(id: NpcResourceId) = NpcResource(id, definitions[id]!!)

}
