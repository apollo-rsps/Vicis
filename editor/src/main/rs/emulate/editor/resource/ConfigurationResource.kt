package rs.emulate.editor.resource

import rs.emulate.editor.resource.provider.ModelResourceProvider
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.MutableConfigDefinition

class ConfigurationResourceProperty<T : Any>(
    name: String,
    type: ConfigPropertyType,
    value: T,
    category: String? = null
) : ResourceProperty<T>(name, value, category)

class ConfigurationResource<out T : MutableConfigDefinition>(
    displayName: String,
    identifier: ResourceIdentifier,
    val config: T,
    properties: List<ConfigurationResourceProperty<*>>,
    private val modelProvider: ((T) -> List<Int>?)? = null
) : Resource(
    displayName,
    identifier,
    properties
) {
    override fun createContentModel(store: ResourceStore): ResourceContentModel {
        if (modelProvider != null) {
            val ids = modelProvider!!(config) ?: return NullContentModel() // !! because kotlinc bug
            println(ids.size)

            return ids.map { id ->
                store.find<ModelResource>(ResourceIdentifier.FileDescriptor(ModelResourceProvider.RESOURCE_INDEX, id))
            }.compoundContentModel()
        }

        return NullContentModel()
    }

}
