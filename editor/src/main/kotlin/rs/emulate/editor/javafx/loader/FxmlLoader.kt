package rs.emulate.editor.javafx.loader

import javafx.scene.Node
import java.net.URL

interface FxmlLoader {
    fun <T> load(fxml: URL) : T
}
