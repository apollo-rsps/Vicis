package rs.emulate.scene3d

/**
 * The primitive type of geometry that the vertex data of a [Geometry] object represents.
 *
 * @param components The number of vertices that make up this geometry.
 */
enum class GeometryType(val components: Int) {
    TRIANGLES(3)
}
