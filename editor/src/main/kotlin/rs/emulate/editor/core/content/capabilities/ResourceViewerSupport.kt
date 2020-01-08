package rs.emulate.editor.core.content.capabilities

import rs.emulate.editor.core.workbench.viewer.ResourceView
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.VirtualFileLoader

interface ResourceViewerSupport<V : VirtualFileId> {
    fun createViewer(id: V, loader: VirtualFileLoader<V>): ResourceView
}
