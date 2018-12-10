package rs.emulate.scene3d.buffer

import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3i


class Vector2ffDataBuffer : GeometryDataBuffer<Vector2f>(2, 4) {
    override fun set(offset: Int, data: Vector2f) {
        data.get(elementOffset(offset), buffer)
    }
}

class Vector3ffDataBuffer : GeometryDataBuffer<Vector3f>(3, 4) {
    override fun set(offset: Int, data: Vector3f) {
        data.get(elementOffset(offset), buffer)
    }
}

class Vector3iDataBuffer : GeometryDataBuffer<Vector3i>(3, 4) {
    override fun set(offset: Int, data: Vector3i) {
        data.get(elementOffset(offset), buffer)
    }
}
