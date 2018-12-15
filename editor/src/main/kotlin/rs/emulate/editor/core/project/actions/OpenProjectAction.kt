package rs.emulate.editor.core.project.actions

import javafx.event.Event
import javafx.scene.Parent
import javafx.scene.control.Alert
import rs.emulate.editor.core.action.WorkbenchAction
import rs.emulate.editor.core.action.annotation.Action
import rs.emulate.editor.core.action.annotation.MenuEntry
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.javafx.loader.FxmlLoader
import javax.inject.Inject

@Action(id = "open-project")
@MenuEntry(categories = ["file"])
class OpenProjectAction @Inject constructor(val fxmlLoader: FxmlLoader, val ctx: WorkbenchContext) : WorkbenchAction {
    override suspend fun handle(event: Event) {
        val dialogContent = fxmlLoader.load<Parent>(this.javaClass.getResource("/rs/emulate/editor/core/project/actions/OpenProjectDialog.fxml"))
        val dialog = Alert(Alert.AlertType.NONE, "Open Project")

        dialog.dialogPane.content = dialogContent
        dialog.showAndWait()
    }

}
