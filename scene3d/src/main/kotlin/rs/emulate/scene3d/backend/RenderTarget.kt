package rs.emulate.scene3d.backend

import rs.emulate.scene3d.backend.opengl.target.OpenGLRenderTarget

interface RenderTarget {
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

    /**
     * Initialize this [RenderTarget] and set up its rendering context and any display resources.
     */
    fun initialize()

    /**
     * Check if the [RenderTarget] has v-sync enabled.
     */
    fun isVsyncEnabled(): Boolean

    /**
     * Resize the framebuffer used by this [RenderTarget] to store its raster data.
     */
    fun resize(width: Int, height: Int)
}
