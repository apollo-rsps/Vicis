package rs.emulate.editor.utils

import javafx.scene.control.Alert
import tornadofx.*
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

fun showExceptionAlert(header: String, it: Throwable) {
    Alert(Alert.AlertType.ERROR).apply {
        headerText = header
        contentText = it.localizedMessage

        dialogPane.content = vbox {
            textarea {
                prefRowCount = 20
                prefColumnCount = 50
                text = stringFromError(it)
            }
        }

        showAndWait()
    }
}

private fun stringFromError(e: Throwable): String {
    val out = ByteArrayOutputStream()
    val writer = PrintWriter(out)
    e.printStackTrace(writer)
    writer.close()
    return out.toString()
}
