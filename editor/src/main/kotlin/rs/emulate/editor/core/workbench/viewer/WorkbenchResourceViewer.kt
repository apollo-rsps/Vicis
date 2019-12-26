package rs.emulate.editor.core.workbench.viewer

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import rs.emulate.editor.core.content.capabilities.ResourceViewerSupport
import rs.emulate.editor.core.workbench.VirtualFileSelection
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.javafx.annotation.FxmlComponent
import rs.emulate.editor.utils.javafx.onChange
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import javax.inject.Inject

@FxmlComponent
class WorkbenchResourceViewer @Inject constructor(
    private val ctx: WorkbenchContext,
    private val viewerSupportMap: @JvmSuppressWildcards Map<ResourceType, ResourceViewerSupport<*>>
) : TabPane() {
    private val tabMap = mutableMapOf<VirtualFileId, Tab>()

    init {
        ctx.selectionProperty.onChange {
            when (it) {
                is VirtualFileSelection<*> -> openView(it)
            }
        }
    }

    fun <V : VirtualFileId> openView(selection: VirtualFileSelection<V>) {
        @Suppress("UNCHECKED_CAST")
        val support = viewerSupportMap[selection.type] as ResourceViewerSupport<V>? ?: return

        val view = support.createViewer(selection.vfsId)
        val tab = tabMap.computeIfAbsent(selection.vfsId) { Tab(selection.vfsId.toString(), view.root) }

        tabs.add(tab)
        selectionModel.select(tab)
    }
}
