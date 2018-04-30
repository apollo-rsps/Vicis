package rs.emulate.editor.workspace.components.opengl.render

/**
 * A rendering target for a [Renderer].
 */
interface RenderTarget {

    /**
     * Bind this [RenderTarget]'s framebuffer as the active framebuffer.
     */
    fun bind()

    /**
     * Blit this [RenderTarget]'s framebuffer to screen.
     */
    fun blit()

    /**
     * Called when this [RenderTarget] is deallocated and no longer used.
     */
    fun dispose()
}
