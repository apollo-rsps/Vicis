package rs.emulate.editor.workspace.components.opengl.render

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.system.MemoryUtil
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A background thread for running OpenGL commands and running user-supplied callbacks
 * within an OpenGL context.
 */
class RenderThread(val target: RenderTarget, val listener: RendererEventListener) : Runnable {

    /**
     * A queue of [RendererCommand]s to be executed on the render thread.
     */
    private val rendererCommandQueue = LinkedBlockingQueue<RendererCommand>()

    /**
     * Flag indicating whether the thread is currently rendering or if it
     * has been paused.
     */
    private val running = AtomicBoolean(true)

    /**
     * Flag indicating whether this thread has been stopped.  When set the thread
     * will exit and deallocate the window.
     */
    private val stopped = AtomicBoolean(false)

    /**
     * Pointer to the offscreen GLFW window backing this threads OpenGL context.
     */
    private var window: Long = 0L

    /**
     * Post a [RendererCommand] to this [RenderThread], to be executed on the next cycle.
     */
    fun post(command: RendererCommand) {
        rendererCommandQueue.offer(command)
    }

    /**
     * Pause this [RenderThread] to be [resume]d later.
     */
    fun pause() {
        running.set(false)
    }

    /**
     * Resume this [RenderThread] after being earlier [pause]d.
     */
    fun resume() {
        running.set(true)
    }

    /**
     * Stop this [RenderThread] and deallocate any resources.
     */
    fun stop() {
        stopped.set(true)
    }

    override fun run() {
        if (!glfwInit()) {
            throw RuntimeException("Unable to initialize GLFW")
        }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(1, 1, "offscreen buffer", MemoryUtil.NULL, MemoryUtil.NULL)

        if (window == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException("Window == null")
        }

        // Bind an OpenGL context for the window to the current thread.
        glfwMakeContextCurrent(window)
        createCapabilities()
        glfwSwapInterval(1)

        // Wait until we've been told to start rendering.
        listener.onStart()

        while (!stopped.get()) {
            while (!rendererCommandQueue.isEmpty()) {
                val command = rendererCommandQueue.poll()

                when (command) {
                    is RendererCommand.Resize -> listener.onResize(command.width, command.height)
                    is RendererCommand.Callback -> command.callback.invoke()
                }
            }

            if (running.get()) {
                target.bind()
                listener.onRender(1.0f)
                target.blit()
            }

            glfwSwapBuffers(window)
        }

        listener.onStop()
        target.dispose()
        glfwDestroyWindow(window)
    }

}
