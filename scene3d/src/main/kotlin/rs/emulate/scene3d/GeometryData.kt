package rs.emulate.scene3d

import rs.emulate.scene3d.buffer.GeometryDataBuffer
import rs.emulate.scene3d.material.VertexAttribute

/**
 * A wrapper around the buffers backing the [VertexAttribute]s of a [Geometry] node.
 */
class GeometryData {

    private val buffers = mutableMapOf<VertexAttribute<*>, GeometryDataBuffer<*>>()

    /**
     * Get or create a [GeometryDataBuffer] representing data for the vertex [attribute].
     */
    fun <T : Any> buffer(attribute: VertexAttribute<T>): GeometryDataBuffer<T> {
        return buffers.computeIfAbsent(attribute) { GeometryDataBuffer.create<T>(attribute.type) } as GeometryDataBuffer<T>
    }
}
