package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.animation.AnimationDefinition
import rs.emulate.legacy.config.animation.AnimationProperty
import rs.emulate.legacy.config.animation.DefaultAnimationDefinition

/**
 * A [Resource] for an [AnimationDefinition].
 */
class AnimationResource(
    id: AnimationResourceId,
    definition: AnimationDefinition
) : ConfigResource<AnimationDefinition>(id, definition) {

    operator fun <T> get(property: AnimationProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "AnimationResource($id) { $properties }"

}

class AnimationResourceBundle(config: Archive) : ConfigResourceBundle<AnimationResourceId, AnimationDefinition> {

    override val idType = AnimationResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { AnimationResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Animation Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id}"
                    }
                }
            }
        }
    }

    override fun load(id: AnimationResourceId) = AnimationResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = AnimationDefinition.ENTRY_NAME,
            definition = AnimationDefinition::class.java,
            default = DefaultAnimationDefinition::class.java
        )
    }

}
