package rs.emulate.editor.workspace.components.opengl.render

/**
 * An OpenGL renderer that spawns a new thread to run OpenGL commands in.
 */
class Renderer(private val renderThread: RenderThread) {

    /**
     * Send a pause signal to the backing [RenderThread].
     */
    fun pause() = renderThread.pause()

    /**
     * Send a resume signal to the backing [RenderThread].
     */
    fun resume() = renderThread.resume()

    /**
     * Post a resize command to this [Renderer]'s [RenderThread].
     */
    fun resize(width: Int, height: Int) = renderThread.post(RendererCommand.Resize(width, height))

    /**
     * Run a [callback] on the next frame executed by this [Renderer]'s [RenderThread].
     */
    fun runLater(callback: () -> Unit) = renderThread.post(RendererCommand.Callback(callback))

    /**
     * Start the [RenderThread] and begin rendering.
     */
    fun start() {
        val thread = Thread(renderThread)
        thread.start()
    }

    /**
     * Send a stop signal to the backing [RenderThread].
     */
    fun stop() = renderThread.stop()

    companion object {

        /**
         * Create a new [Renderer] that blits to the given [target].  [Renderer.stop] *MUST* be called
         * after [create] to deallocate any resources required to create the window.
         */
        fun create(target: RenderTarget, listener: RendererEventListener): Renderer {
            return Renderer(RenderThread(target, listener))
        }
    }
}
