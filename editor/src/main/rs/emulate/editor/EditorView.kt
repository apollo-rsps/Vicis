package rs.emulate.editor

import javafx.application.Platform
import rs.emulate.editor.ui.ResourcePropertyEditorView
import rs.emulate.editor.ui.ResourceTreeView
import rs.emulate.editor.ui.ResourceStoreView
import tornadofx.*


class EditorView : Workspace("Vicis", Workspace.NavigationMode.Tabs) {
    val editorController: EditorController by di()

    val resourceStoreView: ResourceStoreView by inject()
    val resourceTreeView: ResourceTreeView by inject()
    val propertyEditorView: ResourcePropertyEditorView by inject()

    init {
        defaultCreatable = false
        defaultRefreshable = false
        defaultCloseable = false
        defaultComplete = false

        menubar {
            menu("File") {
                item("Exit").action {
                    Platform.exit()
                }
            }
            menu("Window") {
                item("Close all").action {

                }
            }

            menu("Help") {
                item("About...")
            }
        }

        with(leftDrawer) {
            item(resourceTreeView, expanded = true, showHeader = true)
            item(resourceStoreView, showHeader = true)
        }

        with(rightDrawer) {
            addClass(EditorStyles.propertySheet)
            item(propertyEditorView, expanded = false, showHeader = true)
        }

        tabContainer.selectionModel.selectedItemProperty().onChange {
            if (it == null) {
                editorController.activeResourceValue = null
            } else if (it.userData is TabData) {
                editorController.activeResourceValue = (it.userData as TabData).resource
            }
        }

        editorController.setup()
    }
}
