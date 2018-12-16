package rs.emulate.editor.core.content.capabilities

import org.controlsfx.control.PropertySheet
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.VirtualFileLoader

interface ResourcePropertySupport {
    fun createProperties(loader: VirtualFileLoader<out VirtualFileId>, id: VirtualFileId) : List<PropertySheet.Item>
}
