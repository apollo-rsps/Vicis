package rs.emulate.editor.javafx.loader

import java.net.URL
import java.util.*

interface FxmlLoader {
    fun <T> load(fxml: URL, resources: ResourceBundle? = null) : T

    fun <T> load(path: String, resources: ResourceBundle? = null): T = load(this.javaClass.getResource(path))
}
