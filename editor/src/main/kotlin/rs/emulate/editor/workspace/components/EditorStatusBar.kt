package rs.emulate.editor.workspace.components

import com.github.thomasnield.rxkotlinfx.toBinding
import org.controlsfx.control.StatusBar
import tornadofx.*

class EditorStatusBar : EditorComponent() {
    override val root = StatusBar()

    init {
        val id = model.onResourceSelected
            .map { it.id.toString() }
            .toBinding()

        with(root) {
            rightItems.add(label {
                textProperty().bind(id)
            })
        }
    }
}
