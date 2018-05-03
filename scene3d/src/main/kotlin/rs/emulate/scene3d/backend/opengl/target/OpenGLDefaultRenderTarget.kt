package rs.emulate.scene3d.backend.opengl.target

/**
 * No-op [OpenGLRenderTarget] for visible windows that are synchronized
 * through calls to `glfwSwapBuffers()`.
 */
class OpenGLDefaultRenderTarget : OpenGLRenderTarget {
    override fun blit() = Unit
    override fun dispose() = Unit
    override fun bind() = Unit

}
