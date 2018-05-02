package rs.emulate.scene3d.buffer

import glm_.BYTES
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.Vec3i

class Vec2fDataBuffer : GeometryDataBuffer<Vec2>(2, Float.BYTES) {
    override fun set(offset: Int, data: Vec2) {
        data.to(buffer, elementOffset(offset))
    }
}

class Vec3fDataBuffer : GeometryDataBuffer<Vec3>(3, Float.BYTES) {
    override fun set(offset: Int, data: Vec3) {
        data.to(buffer, elementOffset(offset))
    }
}

class Vec3iDataBuffer : GeometryDataBuffer<Vec3i>(3, Int.BYTES) {
    override fun set(offset: Int, data: Vec3i) {
        data.to(buffer, elementOffset(offset))
    }
}
