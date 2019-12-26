package rs.emulate.editor.core.content.capabilities

import rs.emulate.editor.core.workbench.viewer.ResourceView
import rs.emulate.editor.vfs.VirtualFileId

interface ResourceViewerSupport<V: VirtualFileId> {
    fun createViewer(id: V) : ResourceView
}
