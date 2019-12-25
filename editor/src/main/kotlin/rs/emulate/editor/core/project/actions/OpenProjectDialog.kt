package rs.emulate.editor.core.project.actions

import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
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
import java.io.File
import java.nio.file.Files
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

        val isValidBinding = Bindings.createBooleanBinding(
            Callable<Boolean> { validator.validationResult?.messages?.isEmpty() ?: false },
            validator.validationResultProperty()
        )

        openButton.disableProperty().bind(isValidBinding.not())
        openButton.onAction = createAsyncEventHandler(::onOpenAction)
        cancelButton.onAction = createAsyncEventHandler(::onCancelAction)
        vfsRootSelectorButton.onAction = createAsyncEventHandler(::onVfsRootSelectorAction)
    }

    private suspend fun onCancelAction(action: ActionEvent) {
        root.stage?.close()
    }

    private suspend fun onVfsRootSelectorAction(action: ActionEvent) {
        val chooser = Dialogs.createFileChooser(
            LEGACY_CACHE_FILE_NAME to "RuneScape data file",
            MODERN_CACHE_FILE_NAME to "Runescape modern data file"
        )

        val parentResources = Paths.get("..").resolve("resources")
        chooser.initialDirectory = if (Files.exists(parentResources)) {
            parentResources.toFile()
        } else {
            val currentResources = Paths.get(".").resolve("resources")

            if (Files.exists(currentResources)) {
                currentResources.toFile()
            } else {
                File(System.getProperty("user.dir"))
            }
        }

        val selection = chooser.showOpenDialog(root.window)
        selection?.let {
            vfsRootInput.text = it.absolutePath

            if (projectNameInput.text.isNullOrBlank()) {
                projectNameInput.text = it.parentFile.name
            }
        }
    }

    private suspend fun onOpenAction(action: ActionEvent) {
        root.stage?.close()

        val path = Paths.get(vfsRootInput.text)

        val type = when {
            path.endsWith(LEGACY_CACHE_FILE_NAME) -> ProjectType.Legacy
            path.endsWith(MODERN_CACHE_FILE_NAME) -> ProjectType.Modern
            else -> error("Unrecognised cache format")
        }

        val project = projectLoader.loadProject(projectNameInput.text, path, type)
        ctx.openProject(project)
    }

    private companion object {
        private const val LEGACY_CACHE_FILE_NAME = "main_file_cache.dat"
        private const val MODERN_CACHE_FILE_NAME = "main_file_cache.idx255"

    }
}
