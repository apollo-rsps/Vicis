package rs.emulate.editor.javafx

import com.google.inject.AbstractModule
import javafx.util.BuilderFactory
import rs.emulate.editor.javafx.loader.FxmlLoader
import rs.emulate.editor.javafx.loader.GuiceFxmlLoader
import rs.emulate.editor.javafx.loader.factory.ControllerFactory
import rs.emulate.editor.javafx.loader.factory.GuiceBuilderFactory
import rs.emulate.editor.javafx.loader.factory.GuiceControllerFactory

class JavaFxModule : AbstractModule() {
    override fun configure() {
        bind(ControllerFactory::class.java).to(GuiceControllerFactory::class.java)
        bind(BuilderFactory::class.java).to(GuiceBuilderFactory::class.java)
        bind(FxmlLoader::class.java).to(GuiceFxmlLoader::class.java)
    }
}
