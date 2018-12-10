package rs.emulate.editor.javafx.loader.factory

import com.google.inject.Injector
import javax.inject.Inject

class GuiceControllerFactory @Inject constructor(val injector: Injector): ControllerFactory {
    override fun <C : Any> load(type: Class<C>) : C = injector.getInstance(type)
}
