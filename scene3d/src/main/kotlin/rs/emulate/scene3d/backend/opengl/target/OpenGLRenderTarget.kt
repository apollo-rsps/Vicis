package rs.emulate.scene3d.backend.opengl.target

/**
 * A rendering target for a [Renderer].
 */
interface OpenGLRenderTarget {

    /**
     * Bind this [OpenGLRenderTarget]'s framebuffer as the active framebuffer.
     */
    fun bind()

    /**
     * Blit this [OpenGLRenderTarget]'s framebuffer to screen.
     */
    fun blit()

    /**
     * Called when this [OpenGLRenderTarget] is deallocated and no longer used.
     */
    fun dispose()
}
