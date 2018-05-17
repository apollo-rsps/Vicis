package rs.emulate.scene3d.backend.opengl.target

/**
 * No-op [OpenGLRenderTarget] for visible windows that are synchronized
 * through calls to `glfwSwapBuffers()`.
 */
class OpenGLDefaultRenderTarget : OpenGLRenderTarget {
    override fun isVsyncEnabled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resize(width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initialize() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun blit() = Unit
    override fun dispose() = Unit
    override fun bind() = Unit

}
