package rs.emulate.editor.javafx.loader.factory

interface ControllerFactory {
    fun <C: Any> load(type: Class<C>) : C
}
