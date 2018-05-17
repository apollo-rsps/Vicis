package rs.emulate.editor.ui.workspace.components.explorer

import javafx.scene.control.TreeItem
import rs.emulate.editor.ui.workspace.components.EditorTopController
import rs.emulate.editor.utils.javafx.onGuardedChange
import tornadofx.*

class EditorExplorerController : EditorTopController<EditorExplorerModel>() {
    override fun bind(model: EditorExplorerModel) {
        model.rootItem = TreeItem(indexMapper(scope.resourceCache.index()))
        model.selectedItemProperty.onGuardedChange { item ->
            if (item is ExplorerTreeItem.Item) {
                runAsync {
                    scope.resourceCache.load(item.id)
                } success { resource ->
                    scope.resourceSelection = resource
                }
            }
        }
    }
}
