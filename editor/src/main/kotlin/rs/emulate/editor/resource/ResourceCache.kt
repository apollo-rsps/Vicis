package rs.emulate.editor.resource

import rs.emulate.editor.resource.index.ResourceIndexBuilder

class ResourceCache(val bundles: List<ResourceBundle<*>> = emptyList()) {
    private val indexTypeMap = bundles.associateBy(ResourceBundle<*>::idType)

    fun index() = bundles.fold(ResourceIndexBuilder()) { builder, bundle -> builder.also(bundle::index) }.build()

    fun <T : ResourceId> load(resourceId: T): Resource? {
        @Suppress("UNCHECKED_CAST")
        val resourceBundle = indexTypeMap[resourceId::class] as ResourceBundle<T>?
        return resourceBundle?.load(resourceId)
    }
}
