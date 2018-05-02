package rs.emulate.scene3d.backend.opengl

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryUtil.NULL
import rs.emulate.scene3d.Camera
import rs.emulate.scene3d.Geometry
import rs.emulate.scene3d.GeometryType
import rs.emulate.scene3d.Node
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderModule
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderModuleType
import rs.emulate.scene3d.backend.opengl.bindings.OpenGLShaderProgram
import rs.emulate.scene3d.material.VertexAttribute
import kotlin.concurrent.withLock

fun GeometryType.toOpenGLType() = when (this) {
    GeometryType.TRIANGLES -> GL_TRIANGLES
}

/**
 * OpenGL state and container objects for a single geometry node.
 */
class OpenGLGeometryState {

    /**
     * A flag indicating if the OpenGL state has been fully initialized.
     */
    var initialized: Boolean = false

    /**
     * The type of geometry this state represents.
     */
    lateinit var geometryType: GeometryType

    /**
     * The ID of the Vertex Array Object backing
     */
    var vertexArrayObjectId: Int = -1

    /**
     * The IDs of the Vertex Buffer Objects holding the geometry's vertex attributes.
     */
    lateinit var vertexBufferObjectIds: IntArray

    /**
     * The ID of the Vertex Buffer Object containing the indices for rendering indexed geometry.
     * May be empty.
     */
    var vertexIndexBufferObjectId: Int = -1

    /**
     * The shader program this geometry is drawn with.
     */
    lateinit var shader: OpenGLShaderProgram

    /**
     * Create the required OpenGL state from the given [Node].
     */
    fun initialize(node: Geometry) {
        val material = node.material
        val vertexLayout = material.vertexLayout

        val vs = OpenGLShaderModule.create(OpenGLShaderModuleType.VERT, material.vertexShader.load())
        val fs = OpenGLShaderModule.create(OpenGLShaderModuleType.FRAG, material.fragmentShader.load())

        val vaoBuffer = BufferUtils.createIntBuffer(1)
        val vboBuffer = BufferUtils.createIntBuffer(vertexLayout.elements.size)
        val indexVboBuffer = BufferUtils.createIntBuffer(1)

        glGenVertexArrays(vaoBuffer)
        glGenBuffers(vboBuffer)
        glGenBuffers(indexVboBuffer)

        geometryType = node.geometryType
        shader = OpenGLShaderProgram.create(vs, fs)
        vertexArrayObjectId = vaoBuffer[0]
        vertexBufferObjectIds = (0 until vertexLayout.elements.size).map { vboBuffer[it] }.toIntArray()
        vertexIndexBufferObjectId = indexVboBuffer[0]

        update(node)

        initialized = true
    }

    /**
     * Draw the vertex arrays backing this OpenGL state with the parameters supplied by the [Node]'s [Geometry].
     */
    fun draw(camera: Camera, node: Geometry) {
        update(node)

        shader.bind(
            MODEL_MATRIX_UNIFORM to node.worldMatrix,
            PROJECTION_MATRIX_UNIFORM to camera.projectionMatrix,
            VIEW_MATRIX_UNIFORM to camera.viewMatrix
        )

        glBindVertexArray(vertexArrayObjectId)

        if (node.indices.isEmpty()) {
            glDrawArrays(geometryType.toOpenGLType(), 0, node.positions.size)
        } else {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vertexIndexBufferObjectId)
            glDrawElements(geometryType.toOpenGLType(), node.indices.size * geometryType.components, GL_UNSIGNED_INT, 0L)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        }

        glUseProgram(0)
        glBindVertexArray(0)
    }

    /**
     * Update the already initialized VAOs and VBOs with new vertex data supplied by the [Node].
     */
    fun update(node: Geometry) {
        node.lock.withLock {
            glBindVertexArray(vertexArrayObjectId)

            val vertexAttributes = node.material.vertexLayout.elements
            for (vertexAttribute in vertexAttributes) {
                val data = node.data.buffer(vertexAttribute.type)
                val key = vertexAttribute.type.key
                val location = shader.attributeLocations[key]

                if (data.isEmpty() && !vertexAttribute.optional) {
                    throw RuntimeException("Required vertex attribute $key not available in node.")
                } else if (location == null && !vertexAttribute.optional) {
                    throw RuntimeException("Required vertex attribute $key was not found in the shader.  Perhaps it was unused and optimized out?")
                } else if (location == null) {
                    continue
                }

                glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectIds[location])
                glEnableVertexAttribArray(location)
                glBufferData(GL_ARRAY_BUFFER, data.buffer.asFloatBuffer(), GL_STATIC_DRAW)
                glVertexAttribPointer(location, data.components, GL_FLOAT, false, 0, NULL)
                glBindBuffer(GL_ARRAY_BUFFER, 0)
                glBindVertexArray(0)
            }

            if (node.indices.isNotEmpty()) {
                val data = node.data.buffer(VertexAttribute.Index)

                glBindVertexArray(vertexArrayObjectId)
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vertexIndexBufferObjectId)
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.buffer, GL_DYNAMIC_DRAW)
            }

            node.dirty = false
            glBindVertexArray(0)
        }

    }

    companion object {
        const val MODEL_MATRIX_UNIFORM = "model"
        const val PROJECTION_MATRIX_UNIFORM = "projection"
        const val VIEW_MATRIX_UNIFORM = "view"
    }
}
