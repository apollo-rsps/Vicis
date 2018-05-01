package rs.emulate.scene3d.backend.opengl.bindings

import glm_.mat4x4.Mat4
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*

/**
 * A high-level representation of a compiled and linked OpenGL shader program.
 */
class OpenGLShaderProgram(val id: Int, val uniformLocations: Map<String, Int>) {
    //@todo - bridge binding program and setting uniforms
    fun bind() {
        glUseProgram(id)
    }

    fun dispose() {
        glDeleteProgram(id)
    }

    fun setUniform(name: String, value: Mat4) {
        val location = uniformLocations[name] ?: throw RuntimeException("Invalid uniform ${name}")
        val buffer = BufferUtils.createFloatBuffer(16)

        glUniformMatrix4fv(location, false, value to buffer)
    }

    companion object {
        fun create(vararg modules: OpenGLShaderModule): OpenGLShaderProgram {
            val id = glCreateProgram()
            val uniforms = mutableMapOf<String, Int>()

            for (module in modules) {
                glAttachShader(id, module.id)
            }

            glLinkProgram(id)

            if (glGetProgrami(id, GL_LINK_STATUS) <= 0) {
                throw RuntimeException("Failed to link program: ${glGetProgramInfoLog(id)}")
            }

            //@todo - better shader introspection

            val uniformType = BufferUtils.createIntBuffer(1)
            val uniformSize = BufferUtils.createIntBuffer(1)

            val numUniforms = glGetProgrami(id, GL_ACTIVE_UNIFORMS);
            for (uniformLocation in 0 until numUniforms) {
                uniforms[glGetActiveUniform(id, uniformLocation, uniformSize, uniformType)] = uniformLocation
            }

            return OpenGLShaderProgram(id, uniforms)
        }
    }

}
