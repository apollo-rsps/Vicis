package rs.emulate.editor.utils.javafx

import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.stage.FileChooser

object Dialogs {

    fun createFileChooser(vararg extensionFilters: Pair<String, String>): FileChooser {
        val fileChooser = FileChooser()

        fileChooser.extensionFilters += extensionFilters.map { (extension, description) ->
            FileChooser.ExtensionFilter(description, extension)
        }

        return fileChooser
    }

    fun createDialog(content: Node): Alert {
        val dialog = Alert(Alert.AlertType.NONE).apply {
            dialogPane.content = content
        }

        val window = dialog.dialogPane.window
        window.setOnCloseRequest { dialog.dialogPane.stage?.close() }

        return dialog
    }

}
