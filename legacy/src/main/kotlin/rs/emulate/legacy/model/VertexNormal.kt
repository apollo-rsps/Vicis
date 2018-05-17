package rs.emulate.legacy.model

/**
 * A vertex normal.
 *
 * @param x The x point of the VertexNormal.
 * @param y The y point of the VertexNormal.
 * @param z The z point of the VertexNormal.
 * @param faceCount The amount of faces that have this VertexNormal.
 */
data class VertexNormal(val x: Int, val y: Int, val z: Int, val faceCount: Int)
