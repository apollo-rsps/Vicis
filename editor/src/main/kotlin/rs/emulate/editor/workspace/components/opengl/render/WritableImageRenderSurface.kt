package rs.emulate.editor.workspace.components.opengl.render

import javafx.scene.image.WritableImage
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class WritableImageRenderSurface(
    val window: Long,
    var targetBuffer: WritableImageFrameBuffer,
    val executor: ScheduledExecutorService,
    private var target: WritableImage
) {
    private var targetDirty = false

    fun setTarget(image: WritableImage) {
        this.target = image
        this.targetDirty = true
    }

    private fun draw() {
        glfwMakeContextCurrent(window)
        createCapabilities()

        if (targetDirty) {
            targetBuffer.dispose()
            targetBuffer = WritableImageFrameBuffer.create(target.width.toInt(), target.height.toInt())
            targetDirty = false
        }


        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glBegin(GL_TRIANGLES);
        glColor3f(0.1f, 0.2f, 0.8f);
        glVertex3f(0.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glEnd();

        targetBuffer.blit(target.pixelWriter)
    }

    fun start() {
        executor.scheduleWithFixedDelay(
            {
                draw()
            },
            0L,
            1000L / 60,
            TimeUnit.MILLISECONDS
        )
    }

    fun stop() {
        executor.shutdown()
    }

    companion object {
        fun create(target: WritableImage): WritableImageRenderSurface {
            val executor = Executors.newSingleThreadScheduledExecutor()
            val width = target.width.toInt()
            val height = target.height.toInt()

            val (window, renderBuffer) = executor.submit<Pair<Long, WritableImageFrameBuffer>> {
                if (!glfwInit()) {
                    throw RuntimeException("Unable to initialize GLFW")
                }

                glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2)
                glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0)
                glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
                glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
                val window = glfwCreateWindow(width, height, "offscreen targetBuffer", NULL, NULL)
                if (window == NULL) {
                    glfwTerminate()
                    throw RuntimeException("Window == null")
                }

                glfwMakeContextCurrent(window)
                createCapabilities();

                Pair(window, WritableImageFrameBuffer.create(width, height))
            }.get()

            return WritableImageRenderSurface(window, renderBuffer, executor, target)
        }
    }
}
