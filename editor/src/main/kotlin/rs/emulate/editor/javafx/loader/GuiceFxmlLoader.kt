package rs.emulate.editor.javafx.loader

import javafx.fxml.FXMLLoader
import javafx.util.BuilderFactory
import javafx.util.Callback
import rs.emulate.editor.javafx.loader.factory.ControllerFactory
import java.net.URL
import java.util.*
import javax.inject.Inject

class GuiceFxmlLoader @Inject constructor(val builderFactory: BuilderFactory, val controllerFactory: ControllerFactory): FxmlLoader {
    override fun <T> load(fxml: URL, resources: ResourceBundle?): T {
        val loader = FXMLLoader()

        loader.location = fxml
        loader.resources = resources
        loader.controllerFactory =  Callback { it -> it?.let { controllerFactory.load(it) } }
        loader.builderFactory = builderFactory

        return loader.load()
    }

}
