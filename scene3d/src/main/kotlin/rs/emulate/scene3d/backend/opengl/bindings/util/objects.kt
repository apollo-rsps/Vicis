package rs.emulate.scene3d.backend.opengl.bindings.util

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.GL_NO_ERROR
import org.lwjgl.opengl.GL11.glGetError
import java.nio.IntBuffer

fun createGLObject(factory: (IntBuffer) -> Unit) = createGLObject(1, factory)[0]

fun createGLObject(count: Int, factory: (IntBuffer) -> Unit): IntArray {
    val buffer = BufferUtils.createIntBuffer(count)
    factory.invoke(buffer)

    val errors = mutableSetOf<Int>()
    var error = glGetError()

    while (error != GL_NO_ERROR) {
        errors.add(error)
        error = glGetError()
    }

    if (errors.isNotEmpty()) {
        throw RuntimeException("Encountered GL error: $errors")
    }

    return (0 until count).map { buffer[it] }.toIntArray()
}
