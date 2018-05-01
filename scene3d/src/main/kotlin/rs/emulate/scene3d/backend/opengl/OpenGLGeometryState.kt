package rs.emulate.scene3d.backend.opengl

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryUtil
import rs.emulate.scene3d.Camera
import rs.emulate.scene3d.Geometry
import rs.emulate.scene3d.Node
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderModule
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderModuleType
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderProgram
import kotlin.concurrent.withLock

/**
 * OpenGL state and container objects for a single geometry node.
 */
class OpenGLGeometryState {
    /**
     * The ID of the Vertex Array Object backing
     */
    val vertexArrayObject = IntArray(1)

    /**
     * The IDs of the Vertex Buffer Objects holding the geometry's vertex attributes.
     */
    val vertexBufferObjects = IntArray(1)

    /**
     * The ID of the Vertex Buffer Object containing the indices for rendering indexed geometry.
     * May be empty.
     */
    val vertexIndexBufferObject = IntArray(1)

    /**
     * A flag indicating if the OpenGL state has been fully initialized.
     */
    var initialized: Boolean = false

    /**
     * The shader program this geometry is drawn with.
     */
    lateinit var shader: OpenGLShaderProgram

    /**
     * Create the required OpenGL state from the given [Node].
     */
    fun initialize(node: Node) {
        val vs = OpenGLShaderModule.create(OpenGLShaderModuleType.VERT, node.material.vertexShader.load())
        val fs = OpenGLShaderModule.create(OpenGLShaderModuleType.FRAG, node.material.fragmentShader.load())

        shader = OpenGLShaderProgram.create(vs, fs)

        glGenVertexArrays(vertexBufferObjects)
        glGenBuffers(vertexBufferObjects)
        glGenBuffers(vertexIndexBufferObject)

        update(node)

        initialized = true
    }

    /**
     * Draw the vertex arrays backing this OpenGL state with the parameters supplied by the [Node]'s [Geometry].
     */
    fun draw(camera: Camera, node: Node) {
        val geom = node as Geometry

        shader.bind()
        shader.setUniform("view", camera.viewMatrix)
        shader.setUniform("projection", camera.projectionMatrix)
        shader.setUniform("model", node.modelMatrix) //@todo - world matrix

        glBindVertexArray(vertexArrayObject[0])
        glDrawArrays(GL_TRIANGLES, 0, geom.vertices.capacity() / 3)

        glUseProgram(0)
        glBindVertexArray(0)
    }

    /**
     * Update the already initialized VAOs and VBOs with new vertex data supplied by the [Node].
     */
    fun update(node: Node) {
        val geom = node as Geometry

        node.lock.withLock {
            glBindVertexArray(vertexArrayObject[0])

            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjects[0])
            glEnableVertexAttribArray(0)
            glBufferData(GL_ARRAY_BUFFER, geom.vertices, GL_STATIC_DRAW)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, MemoryUtil.NULL)

            node.dirty = false
        }
    }
}
