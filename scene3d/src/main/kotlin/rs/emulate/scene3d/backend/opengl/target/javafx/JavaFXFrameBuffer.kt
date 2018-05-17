package rs.emulate.scene3d.backend.opengl.target.javafx

import javafx.scene.image.PixelFormat
import javafx.scene.image.PixelWriter
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.opengl.GL12.GL_BGRA
import org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41.GL_RGB565
import org.lwjgl.opengl.GL45.*
import rs.emulate.scene3d.backend.opengl.bindings.util.createGLObject
import java.nio.ByteBuffer


/**
 * [fbo] - The id of the FrameBuffer Object returned by OpenGL.
 * [color] - The id of the color RenderBuffer Object returned by OpenGL.
 * [depth] - The id of the depth RenderBuffer Object returned by OpenGL.
 */
class JavaFXFrameBuffer(
    val width: Int,
    val height: Int,
    val fbo: Int,
    val color: Int,
    val depth: Int,
    val pixels: ByteBuffer
) {
    private val pixelsArray = ByteArray(pixels.capacity())

    fun dispose() {
        glDeleteFramebuffers(fbo)
        glDeleteRenderbuffers(color)
        glDeleteRenderbuffers(depth)
    }

    fun copy(destination: PixelWriter) {
        glReadPixels(0, 0, width, height, GL_BGRA, GL_UNSIGNED_BYTE, pixels)

        val rowWidth = width * 4
        for (row in 0 until height) {
            val rowOffset = row * rowWidth
            val rowInverseOffset = pixelsArray.size - rowOffset - rowWidth

            pixels.position(rowInverseOffset)
            pixels.get(pixelsArray, rowOffset, rowWidth)
            pixels.position(0)
        }

        destination.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), pixelsArray, 0, width * 4)
    }

    companion object {
        fun create(width: Int, height: Int): JavaFXFrameBuffer {
            val pixels = BufferUtils.createByteBuffer(width * height * 4)
            val frameBuffer = createGLObject(::glCreateFramebuffers)
            val (colorRenderBuffer, depthRenderBuffer) = createGLObject(2, ::glCreateRenderbuffers)

            glNamedRenderbufferStorage(colorRenderBuffer, GL_RGB565, width, height)
            glNamedFramebufferRenderbuffer(frameBuffer, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderBuffer)
            glNamedRenderbufferStorage(depthRenderBuffer, GL_DEPTH_COMPONENT16, width, height)
            glNamedFramebufferRenderbuffer(frameBuffer, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer)

            return JavaFXFrameBuffer(width, height, frameBuffer, colorRenderBuffer, depthRenderBuffer, pixels)
        }
    }
}
