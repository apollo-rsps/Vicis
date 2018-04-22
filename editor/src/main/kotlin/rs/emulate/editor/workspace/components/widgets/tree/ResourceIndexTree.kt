package rs.emulate.editor.workspace.components.widgets.tree

import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import rs.emulate.editor.workspace.components.EditorComponent
import rs.emulate.editor.workspace.components.widgets.tree.ResourceTreeItem.Category
import rs.emulate.editor.workspace.components.widgets.tree.ResourceTreeItem.Item
import rs.emulate.editor.workspace.components.widgets.tree.ResourceTreeItem.Root
import tornadofx.borderpane
import tornadofx.cellFormat
import tornadofx.get
import tornadofx.populate
import tornadofx.treeview

class ResourceIndexTree : EditorComponent() {
    override val root = borderpane()

    init {
        title = messages["title"]

        with(root) {
            val tree = treeview<ResourceTreeItem>(TreeItem(Root())) {
                selectionModel.selectionMode = SelectionMode.SINGLE

                cellFormat {
                    val item = this.item

                    text = when (item) {
                        is Root -> messages["tree.root_label"]
                        is Category -> item.name
                        is Item -> item.name
                    }
                }
            }

            controller.onResourceIndexChanged.map(::indexMapper).subscribe {
                tree.root = TreeItem(it)
                tree.populate { parent ->
                    val value = parent.value

                    when (value) {
                        is Root -> value.items
                        is Category -> value.items
                        is Item -> emptyList()
                    }
                }
            }

            center = tree
        }
    }
}

