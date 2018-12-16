package rs.emulate.editor.core.project.legacy.npc

import org.controlsfx.control.PropertySheet
import org.controlsfx.property.BeanPropertyUtils
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.VirtualFileLoader
import rs.emulate.legacy.config.npc.NpcDefinition

class NpcPropertySupport : ResourcePropertySupport {
    override fun createProperties(
        loader: VirtualFileLoader<out VirtualFileId>,
        id: VirtualFileId
    ): List<PropertySheet.Item> {
        return BeanPropertyUtils.getProperties(NpcDefinition(1))
    }
}
