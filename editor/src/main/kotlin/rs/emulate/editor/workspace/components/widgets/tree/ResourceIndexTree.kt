package rs.emulate.editor.workspace.components.widgets.tree

import com.github.thomasnield.rxkotlinfx.toObservable
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import rs.emulate.editor.workspace.components.EditorComponent
import rs.emulate.editor.workspace.components.widgets.tree.ResourceTreeItem.Category
import rs.emulate.editor.workspace.components.widgets.tree.ResourceTreeItem.Item
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
            val tree = treeview<ResourceTreeItem>(TreeItem(Category("hidden root node", emptyList()))) {
                isShowRoot = false

                selectionModel.selectionMode = SelectionMode.SINGLE
                selectionModel.selectedItemProperty().toObservable()
                    .filter { it.value is Item }
                    .map { (it.value as Item).id }
                    .distinctUntilChanged()
                    .subscribe(controller::open)

                cellFormat {
                    val item = this.item

                    text = when (item) {
                        is Category -> item.name
                        is Item -> item.name
                    }
                }
            }

            model.onResourceIndexUpdate.map(::indexMapper).subscribe {
                tree.root = TreeItem(it)
                tree.populate { parent ->
                    val value = parent.value

                    when (value) {
                        is Category -> value.items
                        is Item -> emptyList()
                    }
                }
            }

            center = tree
        }
    }
}

