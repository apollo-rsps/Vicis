package rs.emulate.editor.core.workbench.properties.editors

import javafx.application.Platform
import javafx.scene.control.TextInputControl

internal fun enableAutoSelectAll(control: TextInputControl) {
    control.focusedProperty().addListener { _, _, newValue ->
        if (newValue) {
            Platform.runLater(control::selectAll)
        }
    }
}
