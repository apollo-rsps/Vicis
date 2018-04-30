package rs.emulate.editor.workspace.components.opengl.render

/**
 * An event-listener that is notified on the renderer thread whenever an event occurs.
 */
interface RendererEventListener {
    /**
     * Called before the [RenderThread] has started rendering the first frame.
     */
    fun onStart()

    /**
     * Called when a resize event occurs.
     */
    fun onResize(width: Int, height: Int)

    /**
     * Called every with the [delta] time since the last frame.
     */
    fun onRender(delta: Float)

    /**
     * Called when the [RenderThread] is being disposed to deallocate any resources.
     */
    fun onStop()
}
