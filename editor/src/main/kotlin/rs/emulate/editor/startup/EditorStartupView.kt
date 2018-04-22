package rs.emulate.editor.startup

import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.stage.FileChooser.ExtensionFilter
import rs.emulate.editor.utils.PathConverter
import rs.emulate.editor.utils.showExceptionAlert
import rs.emulate.editor.workspace.EditorWorkspaceModel
import rs.emulate.editor.workspace.EditorWorkspaceView
import tornadofx.*
import java.nio.file.Files

/**
 * A startup view that gives the user the opportunity to select the location of their cache data.
 */
class EditorStartupView : View() {
    val controller: EditorStartupController by inject()
    val model: EditorStartupModel by inject()

    /**
     * An array of file extension filters to only show RuneScape cache data files.
     */
    val cacheFileFilters = arrayOf(ExtensionFilter(messages["file_chooser.description"], CACHE_FILE_NAME))

    override val root = form {
        fieldset(messages["label.select_data_file"]) {
            field(messages["field.data_file_path"]) {
                textfield {
                    textProperty().bindBidirectional(model.cacheDataFileProperty, PathConverter())
                }

                button(messages["button.choose"]) {
                    actionEvents()
                        .map { chooseFile(messages["file_chooser.title"], cacheFileFilters).firstOrNull() }
                        .subscribe { it?.let { model.cacheDataFile = it.toPath() } }
                }
            }

            field(messages["field.cache_type"]) {
                choicebox(model.cacheTypeProperty, EditorCacheType.values().toList())
            }
        }

        button(messages["button.continue"]) {
            enableWhen { model.cacheDataFileProperty.booleanBinding { Files.exists(it) } }

            action {
                runAsyncWithProgress {
                    controller.load(model.cacheType, model.cacheDataFile)
                } success { cache ->
                    controller.onCacheLoad.onNext(cache)
                } fail { err ->
                    controller.onCacheLoadError.onNext(err)
                }
            }
        }
    }

    init {
        title = messages["title"]

        controller.onCacheLoad.subscribe {
            val workspaceScope = Scope()
            setInScope(EditorWorkspaceModel(it), workspaceScope)

            replaceWith(find<EditorWorkspaceView>(scope = workspaceScope))
        }

        controller.onCacheLoadError.subscribe {
            showExceptionAlert(messages["label.error_header"], it)
        }
    }

    companion object {
        const val CACHE_FILE_NAME = "main_file_cache.dat"
    }
}
