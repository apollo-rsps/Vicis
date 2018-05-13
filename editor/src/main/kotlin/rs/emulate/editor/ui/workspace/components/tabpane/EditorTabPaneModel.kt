package rs.emulate.editor.ui.workspace.components.tabpane

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.control.Tab
import tornadofx.*

class EditorTabPaneModel : ViewModel() {

    /**
     * A collection of the tabs currently opened in the tab pane.
     */
    val openTabs = FXCollections.observableArrayList<Tab>()

    val selectedTabProperty = SimpleObjectProperty<Tab>()
    var selectedTab: Tab? by selectedTabProperty
}
