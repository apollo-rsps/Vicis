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

const val VIEWER_PROP_KEY = "viewer"
const val RESOURCE_PROP_KEY = "resource"

var Tab.view: ResourceView
    get() = properties[VIEWER_PROP_KEY] as ResourceView
    set(v) {
        properties[VIEWER_PROP_KEY] = v
    }

var Tab.vfsId: VirtualFileId?
    get() = properties[RESOURCE_PROP_KEY] as VirtualFileId
    set(v) {
        properties[RESOURCE_PROP_KEY] = v
    }


@FxmlComponent
class WorkbenchResourceViewer @Inject constructor(
    private val ctx: WorkbenchContext,
    private val viewerSupportMap: @JvmSuppressWildcards Map<ResourceType, ResourceViewerSupport<*>>
) : TabPane() {

    init {
        isFocusTraversable = true

        // @TODO - 2-way binding between tab selection and workbench selection
        ctx.selectionProperty.onChange {
            when (it) {
                is VirtualFileSelection<*> -> openView(it)
            }
        }
    }

    fun <V : VirtualFileId> openView(selection: VirtualFileSelection<V>) {
        @Suppress("UNCHECKED_CAST")
        val support = viewerSupportMap[selection.type] as ResourceViewerSupport<V>? ?: return
        var tab = tabs.find { it.vfsId == selection.vfsId }

        if (tab == null) {
            val view = support.createViewer(selection.vfsId, selection.project.loader)
            tab = Tab(selection.vfsId.toString(), view.root)
            tab.vfsId = selection.vfsId
            tab.view = view
            tab.setOnCloseRequest {
                view.onFocusLost()
                ctx.selection = null
            }

            tabs += tab
        }

        selectionModel.selectedItem?.view?.onFocusLost()
        selectionModel.select(tab)

        tab.view.onFocusGained()
        ctx.selection = selection
    }
}
