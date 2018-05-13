package rs.emulate.editor.ui.workspace.components.status

import javafx.scene.control.Label
import org.controlsfx.control.StatusBar
import rs.emulate.editor.ui.workspace.components.EditorTopView

class EditorStatusBar : EditorTopView<EditorStatusBarController, EditorStatusBarModel>() {
    override val controller by inject<EditorStatusBarController>()
    override val model by inject<EditorStatusBarModel>()

    override val root = StatusBar().apply {
        rightItems.add(Label().apply {
            textProperty().bind(model.footerTextProperty)
        })
    }
}
