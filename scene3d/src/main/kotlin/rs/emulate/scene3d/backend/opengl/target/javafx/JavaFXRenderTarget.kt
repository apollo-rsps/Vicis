package rs.emulate.scene3d.backend.opengl.target.javafx

import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.GL_FALSE
import org.lwjgl.opengl.GL11.glFlush
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.glBindFramebuffer
import org.lwjgl.system.MemoryUtil.NULL
import rs.emulate.scene3d.backend.opengl.target.OpenGLRenderTarget

/**
 * A render target that uses an FBO with color/depth attachments and blits its
 * framebuffer to a [WritableImage], suitable for off-screen rendering.
 */
class JavaFXRenderTarget(val target: ImageView) : OpenGLRenderTarget {
    var windowContext: Long = NULL

    override fun initialize() {
        if (!glfwInit()) {
            throw RuntimeException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

        windowContext = glfwCreateWindow(1, 1, "offscreen buffer", NULL, NULL)

        if (windowContext == NULL) {
            glfwTerminate()
            throw RuntimeException("Window == null")
        }

        // Bind an OpenGL context for the windowContext to the current thread.
        glfwMakeContextCurrent(windowContext)
        createCapabilities()
    }

    override fun isVsyncEnabled() = false

    override fun resize(width: Int, height: Int) {
        buffer?.dispose()
        buffer = JavaFXFrameBuffer.create(width, height)
        bufferImage = WritableImage(width, height)

        Platform.runLater {
            target.image = bufferImage
        }
    }

    private var buffer: JavaFXFrameBuffer? = null
    private var bufferImage = WritableImage(1, 1)

    override fun bind() {
        buffer?.let { glBindFramebuffer(GL_FRAMEBUFFER, it.fbo) }
    }

    override fun blit() {
        glFlush()
        if (target.image == this.bufferImage) {
            buffer?.copy(this.bufferImage.pixelWriter)
        }
    }

    override fun dispose() {
        buffer?.dispose()
    }
}
