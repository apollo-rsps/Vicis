package rs.emulate.editor.resource.bundles.legacy.config.npc

//
//class NpcResourceBundle(config: Archive) : ConfigResourceBundle<NpcResourceId, NpcDefinition> {
//
//    override val idType = NpcResourceId::class
//
//    override val definitions = ConfigEntryDecoder.decode(config, NpcDefinitionDecoder)
//        .associateBy { NpcResourceId(it.id) }
//
//    override fun index(index: ResourceIndexBuilder) {
//        definitions.forEach { (resourceId, def) ->
//            index.category(
//                ConfigResource.INDEX_CATEGORY_NAME) {
//                category("Npc Definitions") {
//                    item {
//                        id = resourceId
//                        label = "${def.id} - ${def.name}"
//                    }
//                }
//            }
//        }
//    }
//
//    override fun load(id: NpcResourceId) = NpcResource(id, definitions[id]!!)
//
//}
