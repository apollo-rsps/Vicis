package rs.emulate.editor.workspace.resource

import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder

class ResourceCache(val bundles: List<ResourceBundle> = emptyList()) {
    fun index() = bundles.fold(ResourceIndexBuilder()) { builder, bundle ->
        bundle.index(builder)
        builder
    }.build()
}
