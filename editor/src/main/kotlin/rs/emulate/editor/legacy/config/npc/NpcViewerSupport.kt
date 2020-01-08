package rs.emulate.editor.legacy.config.npc

import rs.emulate.editor.core.content.capabilities.ResourceViewerSupport
import rs.emulate.editor.core.workbench.viewer.ResourceView
import rs.emulate.editor.core.workbench.viewer.component.SceneComponent
import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.VirtualFileLoader

class NpcViewerSupport : ResourceViewerSupport<LegacyFileId.ConfigEntry> {

    override fun createViewer(
        id: LegacyFileId.ConfigEntry,
        loader: VirtualFileLoader<LegacyFileId.ConfigEntry>
    ): ResourceView {
        return object : ResourceView() {
            override val root = SceneComponent()
        }
    }

}
