package rs.emulate.editor.workspace.components.opengl.render

import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.ARBES2Compatibility.GL_RGB565
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_BGRA
import org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16
import org.lwjgl.opengl.GL30.*
import java.nio.ByteBuffer

/**
 * A render target that uses an FBO with color/depth attachments and blits its
 * framebuffer to a [WritableImage], suitable for off-screen rendering.
 */
class FboRenderTarget(var target: WritableImage) : RenderTarget {
    private var buffer: FboBuffer? = null
    private var pixelsArray = ByteArray(1)

    override fun bind() {
        glReadBuffer(GL_COLOR_ATTACHMENT0)
    }

    override fun blit() {
        val width = target.width.toInt()
        val height = target.height.toInt()
        val destination = target.pixelWriter

        glFinish()

        buffer?.let { buffer ->
            glViewport(0, 0, width, height)
            glReadPixels(0, 0, width, height, GL_BGRA, GL_UNSIGNED_BYTE, buffer.pixels)

            buffer.pixels.get(pixelsArray, 0, pixelsArray.size)
            buffer.pixels.clear().position(0)

            val format = PixelFormat.getByteBgraInstance()
            destination.setPixels(0, 0, width, height, format, pixelsArray, 0, width * 4)
        }
    }

    override fun dispose() {
        buffer?.dispose()
    }

    /**
     * Retarget to a new [WritableImage] and allocate new FBOs / pixel buffers.
     */
    fun retarget(image: WritableImage) {
        val width = image.width.toInt()
        val height = image.height.toInt()

        target = image

        val newBuffer = FboBuffer.create(width, height)
        pixelsArray = ByteArray(newBuffer.pixels.capacity())
        buffer?.dispose()
        buffer = newBuffer
    }
}

/**
 * [fbo] - The id of the FrameBuffer Object returned by OpenGL.
 * [color] - The id of the color RenderBuffer Object returned by OpenGL.
 * [depth] - The id of the depth RenderBuffer Object returned by OpenGL.
 */
private class FboBuffer(val fbo: Int, val color: Int, val depth: Int, val pixels: ByteBuffer) {
    fun dispose() {
        glDeleteFramebuffers(fbo)
        glDeleteRenderbuffers(color)
        glDeleteRenderbuffers(depth)
    }

    companion object {
        fun create(width: Int, height: Int): FboBuffer {
            val pixels = BufferUtils.createByteBuffer(width * height * 4)
            val fbo = IntArray(1)
            glGenFramebuffers(fbo)
            glBindFramebuffer(GL_FRAMEBUFFER, fbo[0])

            val rboColor = IntArray(1)
            glGenRenderbuffers(rboColor)
            glBindRenderbuffer(GL_RENDERBUFFER, rboColor[0])
            glRenderbufferStorage(GL_RENDERBUFFER, GL_RGB565, width, height)
            glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, rboColor[0])

            val rboDepth = IntArray(1)
            glGenRenderbuffers(rboDepth)
            glBindRenderbuffer(GL_RENDERBUFFER, rboDepth[0])
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height)
            glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboDepth[0])

            return FboBuffer(fbo[0], rboColor[0], rboDepth[0], pixels)
        }
    }
}
