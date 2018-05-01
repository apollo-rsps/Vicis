package rs.emulate.scene3d

import glm_.Java.Companion.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3

class Camera(val position: Vec3 = Vec3(0f, 0f, 0f), val target: Vec3 = Vec3(0f, 0f, 0f)) {
    /**
     * A vector representing the current forward direction.
     */
    private val forward = Vec3(WORLD_FORWARD)

    /**
     * A vector representing the current right direction.
     */
    private val right = Vec3(WORLD_RIGHT)

    /**
     * A vector representing the current up direction.
     */
    private val up = Vec3(WORLD_UP)

    /**
     * The view matrix.
     */
    val viewMatrix = Mat4()

    /**
     * The projection matrix.
     */
    val projectionMatrix = Mat4()

    /**
     * Update the [projectionMatrix] of this camera to be a perspective projection with the given
     * parameters.
     */
    fun perspective(width: Int, height: Int, fov: Float, near: Float, far: Float) {
        glm.perspective(projectionMatrix, fov, (width / height).toFloat(), near, far)
        update()
    }

    /**
     * Recalculate the view matrix for this [Camera] with it's local parameters.
     */
    fun update() {
        glm.lookAt(viewMatrix, position, target, up)
    }

    /**
     * Move the camera by the position deltas given relative to it's current orientation.
     */
    fun move(dx: Float, dy: Float, dz: Float) {
        position += right * dx
        position += up * dy
        position += forward * dz

        update()
    }

    companion object {
        val WORLD_RIGHT = Vec3(1f, 0f, 0f)
        val WORLD_UP = Vec3(0f, 1f, 0f)
        val WORLD_FORWARD = Vec3(0f, 0f, 1f)
    }
}
