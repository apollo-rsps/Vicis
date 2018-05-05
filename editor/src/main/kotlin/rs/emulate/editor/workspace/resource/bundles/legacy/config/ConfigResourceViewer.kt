package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.components.widgets.content.ModelResourceViewer
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewer
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewerExtension
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResource
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResourceId
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources
import rs.emulate.legacy.config.`object`.ModelSet
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectProperty
import rs.emulate.legacy.config.item.ItemDefinition
import rs.emulate.legacy.config.item.ItemProperty
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.config.npc.NpcProperty

@SupportedResources(types = [ConfigResource::class])
class ConfigViewerExtension : ResourceViewerExtension { // TODO split into extension per config type

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        val models = when {
            resource.id is NpcResourceId -> npcModels(resource as ConfigResource<NpcDefinition>, cache)
            resource.id is ItemResourceId -> itemModel(resource as ConfigResource<ItemDefinition>, cache)
            resource.id is ObjectResourceId -> objectModels(resource as ConfigResource<ObjectDefinition>, cache)
            else -> throw IllegalArgumentException("Unsupported config resource ${resource.id.javaClass.simpleName}.")
        }

        return ModelResourceViewer(models)
    }

    private fun npcModels(npc: ConfigResource<NpcDefinition>, cache: ResourceCache): List<ModelResource> {
        val modelIds = npc.properties[NpcProperty.Models] as IntArray
        val models = modelIds.asSequence()
            .map { cache.load(ModelResourceId(it)) as ModelResource }
            .map { resource ->
                val recolours = npc.properties[NpcProperty.Colours] as Map<Int, Int>?
                recolours?.let(resource::recoloured) ?: resource
            }

        return models.toList()
    }

    private fun itemModel(item: ConfigResource<ItemDefinition>, cache: ResourceCache): List<ModelResource> {
        val modelId = item.properties[ItemProperty.Model] as Int? ?: return emptyList()
        val model = cache.load(ModelResourceId(modelId)) as ModelResource

        val recolours = item.properties[ItemProperty.Colours] as Map<Int, Int>?
        return listOf(recolours?.let(model::recoloured) ?: model)
    }

    private fun objectModels(obj: ConfigResource<ObjectDefinition>, cache: ResourceCache): List<ModelResource> {
        val modelSet = obj.properties[ObjectProperty.PositionedModels].let { positioned ->
            positioned as ModelSet?

            if (positioned == null || positioned.getModels().isEmpty()) {
                val unpositioned = obj.properties[ObjectProperty.Models] as ModelSet?
                if (unpositioned == null || unpositioned.getModels().isEmpty()) {
                    return emptyList()
                }

                unpositioned
            } else {
                positioned
            }
        }

        val modelId = modelSet.getModel(0)
        val model = cache.load(ModelResourceId(modelId)) as ModelResource

        val recolours = obj.properties[ItemProperty.Colours] as Map<Int, Int>?
        return listOf(recolours?.let(model::recoloured) ?: model)
    }

}
