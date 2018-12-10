package rs.emulate.editor.core.action

interface ContextSensitiveAction : Action {
    fun isAvailable()
}
