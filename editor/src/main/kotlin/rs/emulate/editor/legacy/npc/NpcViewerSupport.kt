package rs.emulate.editor.legacy.npc

import rs.emulate.editor.core.content.capabilities.ResourceViewerSupport
import rs.emulate.editor.core.workbench.viewer.ResourceView
import rs.emulate.editor.core.workbench.viewer.component.SceneComponent
import rs.emulate.editor.vfs.LegacyFileId

class NpcViewerSupport : ResourceViewerSupport<LegacyFileId.ConfigEntry> {
    override fun createViewer(id: LegacyFileId.ConfigEntry): ResourceView {
        // @todo - get Model

        return object : ResourceView() {
            override val root = SceneComponent()
        }
    }
}
