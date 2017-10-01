package rs.emulate.editor.ui.fragments

import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

sealed class Reason(val displayReason: String) {
    object NoResourceOpened : Reason("No resource opened")
    object NoResourceEditorAvailable : Reason("No resource editor or preview window available")
    object NoResourceProperties : Reason("Opened resource has no properties")
}

class EmptyView(val reason: Reason) : Fragment() {
    override val root = vbox { label(reason.displayReason) }

    init {
        with(root) {
            style {
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
                alignment = Pos.CENTER
            }
        }
    }
}