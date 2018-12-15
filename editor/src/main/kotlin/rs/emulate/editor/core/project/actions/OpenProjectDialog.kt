package rs.emulate.editor.core.project.actions

import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import org.controlsfx.validation.ValidationSupport
import org.controlsfx.validation.Validator.combine
import org.controlsfx.validation.Validator.createEmptyValidator
import rs.emulate.editor.core.project.ProjectLoader
import rs.emulate.editor.core.project.ProjectType
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.utils.javafx.Dialogs
import rs.emulate.editor.utils.javafx.createAsyncEventHandler
import rs.emulate.editor.utils.javafx.stage
import rs.emulate.editor.utils.javafx.window
import java.nio.file.Paths
import java.util.concurrent.Callable
import javax.inject.Inject

class OpenProjectDialog @Inject constructor(val ctx: WorkbenchContext, val projectLoader: ProjectLoader) {

    private val validator = ValidationSupport()

    @FXML
    lateinit var root: Pane

    @FXML
    lateinit var openButton: Button

    @FXML
    lateinit var cancelButton: Button

    @FXML
    lateinit var projectNameInput: TextField

    @FXML
    lateinit var vfsRootInput: TextField

    @FXML
    lateinit var vfsRootSelectorButton: Button

    @FXML
    lateinit var vfsTypeInput: ChoiceBox<ProjectType>


    @FXML
    fun initialize() {
        validator.registerValidator(
            projectNameInput,
            true,
            createEmptyValidator<String>("A project name is required")
        )

        validator.registerValidator(
            vfsRootInput,
            true,
            combine(
                createEmptyValidator<String>("A path to the cache is required"),
                VfsRootValidator()
            )
        )

        validator.registerValidator(
            vfsTypeInput,
            true,
            createEmptyValidator<String>("A cache type is required")
        )

        val isValidBinding = Bindings.createBooleanBinding(
            Callable<Boolean> { validator.validationResult?.messages?.isEmpty() ?: false },
            validator.validationResultProperty()
        )

        openButton.disableProperty().bind(isValidBinding.not())
        openButton.onAction = createAsyncEventHandler(this::onOpenAction)
        cancelButton.onAction = createAsyncEventHandler(this::onCancelAction)
        vfsRootSelectorButton.onAction = createAsyncEventHandler(this::onVfsRootSelectorAction)

        vfsTypeInput.items.addAll(ProjectType.Legacy, ProjectType.Modern)
    }

    private suspend fun onCancelAction(action: ActionEvent) {
        root.stage?.close()
    }

    private suspend fun onVfsRootSelectorAction(action: ActionEvent) {
        val fileChooser = Dialogs.createFileChooser(CACHE_FILE_NAME to "RuneScape data file")
        val fileSelection = fileChooser.showOpenDialog(root.window)

        fileSelection?.let { vfsRootInput.text = it.absolutePath }
    }

    private suspend fun onOpenAction(action: ActionEvent) {
        root.stage?.close()

        val project = projectLoader.loadProject(projectNameInput.text, Paths.get(vfsRootInput.text), vfsTypeInput.value)
        ctx.openProject(project)
    }

    companion object {
        const val CACHE_FILE_NAME: String = "main_file_cache.dat"
    }
}
