package rs.emulate.editor.core.project.actions

import javafx.event.Event
import javafx.scene.Parent
import rs.emulate.editor.core.action.WorkbenchAction
import rs.emulate.editor.core.action.annotation.Action
import rs.emulate.editor.core.action.annotation.MenuEntry
import rs.emulate.editor.javafx.loader.FxmlLoader
import rs.emulate.editor.utils.javafx.Dialogs
import javax.inject.Inject

@Action(id = "file.new-project")
@MenuEntry
class OpenProjectAction @Inject constructor(val fxmlLoader: FxmlLoader) : WorkbenchAction {
    override suspend fun handle(event: Event) {
        val dialogContent = fxmlLoader.load<Parent>("/rs/emulate/editor/core/project/actions/OpenProjectDialog.fxml")
        val dialog = Dialogs.createDialog(dialogContent)

        dialog.showAndWait()
    }
}
