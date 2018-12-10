package rs.emulate.editor.core.project.actions

import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import rs.emulate.editor.core.action.Action
import rs.emulate.editor.core.action.annotation.ActionDef
import rs.emulate.editor.core.action.annotation.MenuEntry
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.javafx.loader.FxmlLoader
import tornadofx.show
import javax.inject.Inject

@ActionDef(id = "open-project")
@MenuEntry(categories = ["file"])
class OpenProjectAction @Inject constructor(val fxmlLoader: FxmlLoader, val ctx: WorkbenchContext) : Action {
    override fun handle(event: ActionEvent) {
        val dialog = fxmlLoader.load<Parent>(this.javaClass.getResource("/rs/emulate/editor/core/project/actions/OpenProjectDialog.fxml"))
        val stage = Stage()

        stage.scene = Scene(dialog)
        stage.initOwner(ctx.window)
        stage.show()
    }

}
