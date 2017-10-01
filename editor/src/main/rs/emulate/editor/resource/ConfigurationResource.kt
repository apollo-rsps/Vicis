package rs.emulate.editor.resource

import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.MutableConfigDefinition

class ConfigurationResourceProperty<T : Any>(
    name: String,
    type: ConfigPropertyType,
    value: T,
    category: String? = null
) : ResourceProperty<T>(name, value, category)

class ConfigurationResource<out T : MutableConfigDefinition> : Resource {
    override fun createContentModel(store: ResourceStore): ResourceContentModel {
        return NullContentModel()
    }

    val config: T

    constructor(
        displayName: String,
        identifier: ResourceIdentifier,
        config: T,
        properties: List<ConfigurationResourceProperty<*>>
    ) : super(
        displayName,
        identifier,
        properties
    ) {
        this.config = config
    }
}