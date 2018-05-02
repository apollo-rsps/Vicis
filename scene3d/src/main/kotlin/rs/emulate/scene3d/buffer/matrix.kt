package rs.emulate.scene3d.buffer

import glm_.BYTES
import glm_.mat4x4.Mat4

class Mat4fDataBuffer : GeometryDataBuffer<Mat4>(4 * 4, Float.BYTES) {
    override fun set(offset: Int, data: Mat4) {
        data.to(buffer, elementOffset(offset))
    }
}
