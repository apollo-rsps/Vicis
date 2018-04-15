package rs.emulate.editor.ui

import javafx.collections.FXCollections
import javafx.scene.layout.Priority
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.EditorController
import rs.emulate.editor.EditorStyles
import rs.emulate.editor.ui.fragments.EmptyView
import rs.emulate.editor.ui.fragments.Reason
import tornadofx.*

class ResourcePropertyEditorView : View("Properties") {
    val controller: EditorController by di()

    override val root = vbox {
        add(EmptyView(Reason.NoResourceOpened))
    }

    init {
        with(root) {
            controller.activeResource.onChange {
                when {
                    it == null -> replaceChildren(EmptyView(Reason.NoResourceOpened))
                    it.properties.isEmpty() -> replaceChildren(EmptyView(Reason.NoResourceProperties))
                    else -> {
                        val propertyList = FXCollections.observableArrayList<PropertySheet.Item>(it.properties)
                        val propertySheet = PropertySheet(propertyList)

                        propertySheet.addClass(EditorStyles.propertySheet)
                        propertySheet.style {
                            vgrow = Priority.ALWAYS
                            hgrow = Priority.SOMETIMES
                        }

                        replaceChildren(propertySheet)
                    }
                }
            }
        }

    }
}
