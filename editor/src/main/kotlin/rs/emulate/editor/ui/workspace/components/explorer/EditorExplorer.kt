package rs.emulate.editor.ui.workspace.components.explorer

import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import rs.emulate.editor.ui.workspace.components.EditorTopView
import tornadofx.*

class EditorExplorer : EditorTopView<EditorExplorerController, EditorExplorerModel>() {

    override val model by inject<EditorExplorerModel>()
    override val controller by inject<EditorExplorerController>()

    override val root = treeview(ROOT_NODE) {
        title = messages["title"]

        isShowRoot = false
        selectionModel.selectionMode = SelectionMode.SINGLE

        setOnMouseClicked {
            // Only select resources on double click
            if (it.clickCount == 2) {
                model.selectedItem = selectionModel.selectedItem.value
            }
        }

        cellFormat {
            val item = this.item

            text = when (item) {
                is ExplorerTreeItem.Category -> item.name
                is ExplorerTreeItem.Item -> item.name
            }
        }
    }

    init {
        model.rootItemProperty.onChange {
            root.root = it
            root.populate { parent ->
                val value = parent.value

                when (value) {
                    is ExplorerTreeItem.Category -> value.items
                    is ExplorerTreeItem.Item -> emptyList()
                }
            }
        }
    }

    companion object {
        val ROOT_NODE = TreeItem<ExplorerTreeItem>(ExplorerTreeItem.Category("hidden root node", emptyList()))
    }
}

