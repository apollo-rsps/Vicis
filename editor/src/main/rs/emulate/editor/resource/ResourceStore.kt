package rs.emulate.editor.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rs.emulate.editor.resource.provider.ResourceProvider
import rs.emulate.editor.resource.provider.ResourceProviderResult

@Component
class ResourceStore {
    @Autowired lateinit var resourceProviders: List<ResourceProvider<*>>

    private val cache = mutableMapOf<ResourceIdentifier, Resource>()

    fun <T : Resource> find(id: ResourceIdentifier): T {
        cache[id]?.let { return it as T }

        val result = resourceProviders.map { it.provide(id) }.first { it !is ResourceProviderResult.NotSupported }

        when (result) {
            is ResourceProviderResult.NotFound<*> -> throw IllegalArgumentException("Invalid resource identifier")
            is ResourceProviderResult.Found<*> -> {
                cache[id] = result.resource
                return result.resource as T
            }
            else -> throw IllegalStateException("Invalid branch $result")
        }
    }
}
