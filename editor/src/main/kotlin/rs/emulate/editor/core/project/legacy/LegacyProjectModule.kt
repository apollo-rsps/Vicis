package rs.emulate.editor.core.project.legacy

import com.google.inject.AbstractModule
import com.google.inject.multibindings.MapBinder
import rs.emulate.editor.core.content.LegacyResourceType
import rs.emulate.editor.core.content.ResourceType
import rs.emulate.editor.core.content.capabilities.ResourceContentSupport
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.utils.guice.newMapBinder

class LegacyProjectModule : AbstractModule() {
    override fun configure() {
        val propertySupportBinder = newMapBinder<ResourceType, ResourcePropertySupport>(binder())
        val contentSupportBinder = newMapBinder<ResourceType, ResourceContentSupport>(binder())

    }
}
