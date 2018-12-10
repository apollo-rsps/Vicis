package rs.emulate.scene3d.buffer

import org.joml.Matrix4f

class Matrix4ffDataBuffer : GeometryDataBuffer<Matrix4f>(4 * 4, 4) {
    override fun set(offset: Int, data: Matrix4f) {
        data.get(elementOffset(offset), buffer)
    }
}
