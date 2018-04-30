package rs.emulate.editor.workspace.components.opengl.render

/**
 * A command that can be dispatched to a [RenderThread] for execution within a GL context.
 */
sealed class RendererCommand {

    /**
     * A command that instructs the [RenderThread] to emit a resize event in the GL thread.
     */
    data class Resize(val width: Int, val height: Int) : RendererCommand()

    /**
     * A command with a callback to be run in the GL thread.
     */
    data class Callback(val callback: () -> Unit) : RendererCommand()
}
