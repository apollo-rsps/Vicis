package rs.emulate.editor.core.project.actions

import com.dlsc.formsfx.model.structure.Field
import com.dlsc.formsfx.model.structure.Form
import com.dlsc.formsfx.model.structure.Group
import com.dlsc.formsfx.view.renderer.FormRenderer
import com.dlsc.formsfx.view.util.ColSpan
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import rs.emulate.editor.ui.startup.EditorCacheType
import tornadofx.enableWhen

class OpenProjectDialog {
    @FXML
    lateinit var openButton: Button

    @FXML
    lateinit var cancelButton: Button

    @FXML
    lateinit var formPane: ScrollPane

    @FXML
    fun initialize() {
        val form = Form.of(
            Group.of(
                Field.ofStringType("cache")
                    .label("Project name")
                    .required(true)
            ),
            Group.of(
                Field.ofSingleSelectionType(listOf(EditorCacheType.Legacy, EditorCacheType.Modern))
                    .labelDescription("Game data version")
                    .required(true),

                Field.ofStringType("")
                    .labelDescription("Game data location")
                    .required(true)
            )
        )

        openButton.enableWhen(form.validProperty())

        val element = FormRenderer(form)

        formPane.content = element
    }
}
