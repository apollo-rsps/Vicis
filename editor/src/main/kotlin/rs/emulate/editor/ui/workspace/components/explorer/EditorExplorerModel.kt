package rs.emulate.editor.ui.workspace.components.explorer

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TreeItem
import tornadofx.*

class EditorExplorerModel : ViewModel() {
    val rootItemProperty = SimpleObjectProperty<TreeItem<ExplorerTreeItem>>()
    var rootItem: TreeItem<ExplorerTreeItem> by rootItemProperty

    val selectedItemProperty = SimpleObjectProperty<ExplorerTreeItem>()
    var selectedItem : ExplorerTreeItem? by selectedItemProperty
}
