package rs.emulate.scene3d.backend.opengl.bindings

import glmType
import glm_.mat2x2.Mat2
import glm_.mat3x3.Mat3
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import rs.emulate.scene3d.buffer.GeometryDataBuffer
import kotlin.reflect.KClass

/**
 * A high-level representation of a compiled and linked OpenGL shader program.
 *
 * @param id The unique ID of the shader program returned by OpenGL.
 * @param uniformTypes A mapping of uniform names to the index they occur at.
 * @param uniformBuffers A mapping of uniform locations to th buffers representing them.
 */
class OpenGLShaderProgram(
    private val id: Int,
    val attributeLocations: Map<String, Int>,
    val attributeTypes: Map<String, KClass<out Any>>,
    private val uniformBuffers: Map<String, GeometryDataBuffer<out Any>>,
    private val uniformLocations: Map<String, Int>
) {
    /**
     * Bind this shader program as active and bind the [uniforms] to
     * their respective locations.
     */
    fun bind(vararg uniforms: Pair<String, Any>) {
        glUseProgram(id)

        for ((name, value) in uniforms) {
            val location = uniformLocations[name] ?: throw IllegalArgumentException("No uniform named ${name}")
            val data = uniformBuffers[name] as GeometryDataBuffer<Any>
            data.set(value)

            val buffer = data.buffer.asFloatBuffer()
            buffer.position(0)

            when (value) {
                is Vec2 -> glUniform2fv(location, buffer)
                is Vec3 -> glUniform3fv(location, buffer)
                is Vec4 -> glUniform4fv(location, buffer)
                is Mat2 -> glUniformMatrix2fv(location, false, buffer)
                is Mat3 -> glUniformMatrix3fv(location, false, buffer)
                is Mat4 -> glUniformMatrix4fv(location, false, buffer)
            }
        }

        glValidateProgram(id)
        if (glGetProgrami(id, GL_VALIDATE_STATUS) <= 0) {
            throw RuntimeException("Failed to validate program: ${glGetProgramInfoLog(id)}")
        }
    }

    fun dispose() {
        glDeleteProgram(id)
    }

    companion object {
        fun create(vararg modules: OpenGLShaderModule): OpenGLShaderProgram {
            val id = glCreateProgram()
            val attributeLocations = mutableMapOf<String, Int>()
            val attributeTypes = mutableMapOf<String, KClass<out Any>>()
            val uniformBuffers = mutableMapOf<String, GeometryDataBuffer<out Any>>()
            val uniformLocations = mutableMapOf<String, Int>()

            modules.forEach { glAttachShader(id, it.id) }
            glLinkProgram(id)

            if (glGetProgrami(id, GL_LINK_STATUS) <= 0) {
                throw RuntimeException("Failed to link program: ${glGetProgramInfoLog(id)}")
            }

            val typeBuffer = BufferUtils.createIntBuffer(1)
            val sizeBuffer = BufferUtils.createIntBuffer(1)
            val attributesLength = glGetProgrami(id, GL_ACTIVE_ATTRIBUTES)

            for (attributeOffset in 0 until attributesLength) {
                val name = glGetActiveAttrib(id, attributeOffset, sizeBuffer, typeBuffer)
                val loc = glGetAttribLocation(id, name)

                attributeLocations[name] = loc
                attributeTypes[name] = glmType(typeBuffer[0])
            }

            val uniformsLength = glGetProgrami(id, GL_ACTIVE_UNIFORMS);

            for (uniformOffset in 0 until uniformsLength) {
                val name = glGetActiveUniform(id, uniformOffset, sizeBuffer, typeBuffer)
                val loc = glGetUniformLocation(id, name)

                val type = glmType(typeBuffer[0])
                val size = sizeBuffer[0]
                if (size > 1) {
                    throw IllegalStateException("Uniform arrays are currently unsupported")
                }

                val buffer = GeometryDataBuffer.create(type)

                uniformLocations[name] = loc
                uniformBuffers[name] = buffer
            }

            return OpenGLShaderProgram(
                id,
                attributeLocations,
                attributeTypes,
                uniformBuffers,
                uniformLocations
            )
        }
    }
}
