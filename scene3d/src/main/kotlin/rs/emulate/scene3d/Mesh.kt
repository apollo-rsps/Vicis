package rs.emulate.scene3d

import org.lwjgl.BufferUtils

class Mesh : Node(), Geometry {
    override var dirty = true
    override val vertexSize = 3

    override var geometryType = GeometryType.TRIANGLES
    override var vertices = BufferUtils.createFloatBuffer(0)
}
