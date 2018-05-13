package rs.emulate.editor.ui.workspace.components.status

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class EditorStatusBarModel : ViewModel() {
    val footerTextProperty = SimpleStringProperty()
    var footerText by footerTextProperty
}
