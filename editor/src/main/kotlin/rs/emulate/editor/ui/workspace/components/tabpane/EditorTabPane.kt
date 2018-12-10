package rs.emulate.editor.ui.workspace.components.tabpane

import javafx.beans.binding.Bindings
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.ui.widgets.content.ResourceViewer
import rs.emulate.editor.ui.workspace.components.EditorTopView
import rs.emulate.editor.utils.javafx.addGuardedListener
import rs.emulate.editor.utils.javafx.onGuardedChange
import tornadofx.vboxConstraints
import kotlin.collections.set

const val VIEWER_PROP_KEY = "viewer"
const val RESOURCE_PROP_KEY = "resource"

var Tab.resourceViewer: ResourceViewer?
    get() = properties[VIEWER_PROP_KEY] as? ResourceViewer
    set(v) {
        properties[VIEWER_PROP_KEY] = v
    }

var Tab.resource: Resource?
    get() = properties[RESOURCE_PROP_KEY] as? Resource
    set(v) {
        properties[RESOURCE_PROP_KEY] = v
    }

class EditorTabPane : EditorTopView<EditorTabPaneController, EditorTabPaneModel>() {
    override val controller: EditorTabPaneController by inject()
    override val model: EditorTabPaneModel by inject()

    override val root = TabPane().apply {
        tabClosingPolicy = TabPane.TabClosingPolicy.ALL_TABS

        vboxConstraints {
            vGrow = Priority.ALWAYS
        }

        selectionModel.selectedItemProperty().addGuardedListener { _, old, new ->
            old?.resourceViewer?.onFocusLost()

            new?.let {
                it.content?.requestFocus()
                it.resourceViewer?.onFocusGained()
            }

            model.selectedTab = new
        }
    }

    init {
        model.selectedTabProperty.onGuardedChange {
            root.selectionModel.select(it)
        }

        Bindings.bindContentBidirectional(model.openTabs, root.tabs)
    }
}
