package rs.emulate.editor.javafx.loader

import java.net.URL

interface FxmlLoader {
    fun <T> load(fxml: URL) : T

    fun <T> load(path: String): T = load(this.javaClass.getResource(path))
}
