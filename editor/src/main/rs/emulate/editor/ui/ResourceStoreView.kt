package rs.emulate.editor.ui

import javafx.scene.layout.Priority
import rs.emulate.editor.EditorController
import tornadofx.*


class ResourceStoreView : View("Open resources") {
    private val controller: EditorController by di()

    override val root = vbox()

    init {
        with(root) {
            listview(controller.resourcesOpened) {
                style {
                    vgrow = Priority.ALWAYS
                }

                onUserSelect { controller.activeResourceValue = it }

                cellFormat {
                    text = it.name()
                }
            }
        }
    }
}
