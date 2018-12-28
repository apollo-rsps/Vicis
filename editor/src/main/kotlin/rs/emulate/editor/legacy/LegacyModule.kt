package rs.emulate.editor.legacy

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.MapBinder
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.legacy.npc.NpcPropertySupport
import rs.emulate.editor.vfs.LegacyResourceType
import rs.emulate.editor.vfs.ResourceType

class LegacyModule : AbstractModule() {
    override fun configure() {
        val binder = MapBinder.newMapBinder(
            binder(),
            TypeLiteral.get(ResourceType::class.java),
            object : TypeLiteral<ResourcePropertySupport<*>>() {}
        )

        binder.addBinding(LegacyResourceType.Npc).to(NpcPropertySupport::class.java)
    }
}
