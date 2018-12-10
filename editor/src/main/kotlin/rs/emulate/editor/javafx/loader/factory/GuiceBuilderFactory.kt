package rs.emulate.editor.javafx.loader.factory

import com.google.inject.Injector
import javafx.fxml.JavaFXBuilderFactory
import javafx.util.Builder
import javafx.util.BuilderFactory
import rs.emulate.editor.javafx.annotation.FxmlComponent
import javax.inject.Inject

class GuiceBuilderFactory @Inject constructor(val injector: Injector) : BuilderFactory {
    val defaultBuilder = JavaFXBuilderFactory()

    override fun getBuilder(type: Class<*>): Builder<*>? {
        val ty = type
        val isManagedType = ty.annotations.any { it.annotationClass == FxmlComponent::class }

        return if (isManagedType) {
            Builder { injector.getInstance(type) }
        } else {
            defaultBuilder.getBuilder(type) ?: return null
        }
    }
}
