package rs.emulate.editor.startup

import com.github.thomasnield.rxkotlinfx.actionEvents
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import javafx.scene.control.ButtonType
import javafx.stage.FileChooser.ExtensionFilter
import rs.emulate.editor.utils.PathConverter
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
        }

        button(messages["button.continue"]) {
            enableWhen { model.cacheDataFileProperty.booleanBinding { Files.exists(it) } }
            actionEvents().subscribe { controller.load(model.cacheDataFile) }
        }
    }

    init {
        title = messages["title"]

        //@todo - raising an error causes the observable to be unsubscribed.  what's the idiomatic way to do this?
        controller.onCacheLoaded.onErrorResumeNext(Observable.empty()).subscribeBy(
            onNext = {
                replaceWith(EditorWorkspaceView(it))
            },
            onError = {
                error(messages["label.error_header"], it.localizedMessage, ButtonType.CLOSE)
            }
        )
    }

    companion object {
        const val CACHE_FILE_NAME = "main_file_cache.dat"
    }
}
