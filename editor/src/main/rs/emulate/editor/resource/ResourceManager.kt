package rs.emulate.editor.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rs.emulate.editor.resource.provider.ResourceProvider
import rs.emulate.editor.ui.ResourceContentViewerFactory

@Component
class ResourceManager() {
    @Autowired lateinit var providers: List<ResourceProvider<*>>

    private val openResources = mutableListOf<Resource>()

    public fun providers(): List<ResourceProvider<*>> {
        return providers
    }

    fun open(resource: Resource) {
        openResources.add(resource)
    }

    fun getOpenResources(): List<Resource> {
        return openResources
    }
}