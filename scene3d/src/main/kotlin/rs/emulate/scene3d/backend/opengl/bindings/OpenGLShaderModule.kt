package rs.emulate.scene3d.backend.opengl.bindings

import org.lwjgl.opengl.GL20.*

/**
 * A high-level representation of a compiled shader module.
 */
class OpenGLShaderModule(val id: Int) {
    fun dispose() {
        glDeleteShader(id)
    }

    companion object {
        fun create(type: OpenGLShaderModuleType, source: String): OpenGLShaderModule {
            val id = glCreateShader(when (type) {
                OpenGLShaderModuleType.FRAG -> GL_FRAGMENT_SHADER
                OpenGLShaderModuleType.VERT -> GL_VERTEX_SHADER
            })

            glShaderSource(id, source)
            glCompileShader(id)

            //@todo - check compile status
            return OpenGLShaderModule(id)
        }
    }
}
