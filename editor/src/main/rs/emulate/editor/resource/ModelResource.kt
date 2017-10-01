package rs.emulate.editor.resource

import rs.emulate.legacy.model.Model

class ModelResource(displayName: String, identifier: ResourceIdentifier, model: Model) : Resource(
    displayName,
    identifier
) {
    override fun createContentModel(store: ResourceStore): ResourceContentModel {
        return NullContentModel()
    }
}