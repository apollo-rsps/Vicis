package rs.emulate.scene3d.backend.opengl.target.javafx

import javafx.scene.image.PixelFormat
import javafx.scene.image.PixelWriter
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.ARBES2Compatibility
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_BGRA
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30.*
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

        pixels.get(pixelsArray, 0, pixelsArray.size)
        pixels.position(0)

        destination.setPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), pixelsArray, 0, width * 4)
    }

    companion object {
        fun create(width: Int, height: Int): JavaFXFrameBuffer {
            val pixels = BufferUtils.createByteBuffer(width * height * 4)
            val fbo = IntArray(1)
            glGenFramebuffers(fbo)
            glBindFramebuffer(GL_FRAMEBUFFER, fbo[0])

            val rboColor = IntArray(1)
            glGenRenderbuffers(rboColor)
            glBindRenderbuffer(GL_RENDERBUFFER, rboColor[0])
            glRenderbufferStorage(GL_RENDERBUFFER, ARBES2Compatibility.GL_RGB565, width, height)
            glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, rboColor[0])

            val rboDepth = IntArray(1)
            glGenRenderbuffers(rboDepth)
            glBindRenderbuffer(GL_RENDERBUFFER, rboDepth[0])
            glRenderbufferStorage(GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT16, width, height)
            glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboDepth[0])

            return JavaFXFrameBuffer(width, height, fbo[0], rboColor[0], rboDepth[0], pixels)
        }
    }
}
