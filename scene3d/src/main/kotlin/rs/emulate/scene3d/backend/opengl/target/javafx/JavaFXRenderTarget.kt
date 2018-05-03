package rs.emulate.scene3d.backend.opengl.target.javafx

import javafx.scene.image.WritableImage
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import rs.emulate.scene3d.backend.opengl.target.OpenGLRenderTarget

/**
 * A render target that uses an FBO with color/depth attachments and blits its
 * framebuffer to a [WritableImage], suitable for off-screen rendering.
 */
class JavaFXRenderTarget(target: WritableImage) : OpenGLRenderTarget {

    private var actualTarget: WritableImage = target
    private var buffer: JavaFXFrameBuffer? = null
    private var pixelsArray = ByteArray(0)

    var dirty: Boolean = true
    var target: WritableImage = target
        set(v) {
            field = v
            dirty = true
        }

    override fun bind() {
        if (dirty) {
            val width = target.width.toInt()
            val height = target.height.toInt()
            val newBuffer = JavaFXFrameBuffer.create(width, height)

            pixelsArray = ByteArray(newBuffer.pixels.capacity())
            buffer?.dispose()
            buffer = newBuffer
            actualTarget = target
            dirty = false
        }

        glReadBuffer(GL_COLOR_ATTACHMENT0)
    }

    override fun blit() {
        glFlush()
        buffer?.copy(actualTarget.pixelWriter)
    }

    override fun dispose() {
        buffer?.dispose()
    }
}
