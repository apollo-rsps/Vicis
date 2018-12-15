package rs.emulate.editor.core.project.actions

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.stage.Stage
import rs.emulate.editor.core.project.ProjectFactory
import rs.emulate.editor.core.project.createProject
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.utils.formfx.createFormBuilder
import rs.emulate.editor.utils.javafx.createAsyncEventHandler
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

class OpenProjectDialog @Inject constructor(val ctx: WorkbenchContext, val projectLoader: ProjectFactory) {
    @FXML
    lateinit var openButton: Button

    @FXML
    lateinit var cancelButton: Button

    @FXML
    lateinit var formPane: ScrollPane

    private val model = OpenProjectModel()

    @FXML
    fun initialize() {
        val form = createFormBuilder()
            .include("projectName", "vfsRoot", "vfsType")
            .source(model)
            .build()

        formPane.content = form
        openButton.onAction = createAsyncEventHandler(this::onOpenAction)
        cancelButton.onAction = createAsyncEventHandler(this::onCancelAction)
    }

    private suspend fun onCancelAction(action: ActionEvent) {
        (formPane.scene.window as Stage).close()
    }

    private suspend fun onOpenAction(action: ActionEvent) {
        val vfsRootPath = Paths.get(model.vfsRoot.get())
        val vfsActualRootPath = if (Files.isDirectory(vfsRootPath)) vfsRootPath else vfsRootPath.parent
        val project = projectLoader.createProject(model.projectName.get(), vfsActualRootPath, model.vfsType.get())

        ctx.openProject(project)
    }
}
