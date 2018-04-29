package rs.emulate.editor.workspace.components.opengl.render

import javafx.scene.image.PixelFormat
import javafx.scene.image.PixelWriter
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.ARBES2Compatibility
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_BGRA
import org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16
import org.lwjgl.opengl.GL30.*

/**
 * A wrapper around a FrameBuffer Object that blits its framebuffer to a [WritableImage]'s pixel writer.
 */
class WritableImageFrameBuffer(val width: Int, val height: Int, val fbo: Int, val colorRbo: Int, val depthRbo: Int) {
    private val pixelBuffer = BufferUtils.createByteBuffer(width * height * 4)
    private val pixelBufferArray = ByteArray(width * height * 4)

    @Synchronized
    fun blit(destination: PixelWriter) {
        glFlush()
        glViewport(0, 0, width, height)
        glReadPixels(0, 0, width, height, GL_BGRA, GL_UNSIGNED_BYTE, pixelBuffer)

        pixelBuffer.get(pixelBufferArray, 0, pixelBufferArray.size)
        pixelBuffer.clear().position(0)

        val pixelFormat = PixelFormat.getByteBgraInstance()
        destination.setPixels(0, 0, width, height, pixelFormat, pixelBufferArray, 0, width * 4)
    }

    fun dispose() {
        glDeleteFramebuffers(fbo)
        glDeleteRenderbuffers(colorRbo)
        glDeleteRenderbuffers(depthRbo)
    }

    companion object {
        fun create(width: Int, height: Int): WritableImageFrameBuffer {
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
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height)
            glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboDepth[0])

            glReadBuffer(GL_COLOR_ATTACHMENT0)

            return WritableImageFrameBuffer(width, height, fbo[0], rboColor[0], rboDepth[0])
        }
    }
}
