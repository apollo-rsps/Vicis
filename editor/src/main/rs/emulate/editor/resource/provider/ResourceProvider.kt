package rs.emulate.editor.resource.provider

import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceIdentifier

class InvalidResourceIdentifierException(message: String) : Exception(message)

sealed class ResourceProviderResult<out T : Resource> {
    class NotSupported<out T : Resource> : ResourceProviderResult<T>()
    class Found<out T : Resource>(val resource: T) : ResourceProviderResult<T>()
    class NotFound<out T : Resource>() : ResourceProviderResult<T>()
}

interface ResourceProvider<out T : Resource> {
    abstract val lazyLoads: Boolean

    fun resourceName(): String

    fun provide(identifier: ResourceIdentifier): ResourceProviderResult<T>

    fun listAll(): List<ResourceIdentifier>

}