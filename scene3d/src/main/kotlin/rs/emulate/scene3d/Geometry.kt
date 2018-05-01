package rs.emulate.scene3d

import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Specification for a [Node] that has vertex data associated with it.
 */
interface Geometry {
    /**
     * The type of geometry the vertex data is rendered as.
     */
    var geometryType: GeometryType

    /**
     * Backing buffer for the vertex data. @todo split this out into vertex attributes / vertex layout
     */
    var vertices: FloatBuffer
}
