package rs.emulate.scene3d

import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(val position: Vector3f = Vector3f(0f, 0f, 0f), val target: Vector3f = Vector3f(0f, 0f, 0f)) {
    /**
     * A vector representing the current forward direction.
     */
    private val forward = Vector3f(WORLD_FORWARD)

    /**
     * A vector representing the current right direction.
     */
    private val right = Vector3f(WORLD_RIGHT)

    /**
     * A vector representing the current up direction.
     */
    private val up = Vector3f(WORLD_UP)

    /**
     * The view matrix.
     */
    val viewMatrix = Matrix4f()

    /**
     * The projection matrix.
     */
    val projectionMatrix = Matrix4f()

    /**
     * Update the [projectionMatrix] of this camera to be a perspective projection with the given
     * parameters.
     */
    fun perspective(width: Int, height: Int, fov: Float, near: Float, far: Float) {
        projectionMatrix.perspective(fov, width.toFloat() / height.toFloat(), near, far)
        update()
    }

    /**
     * Recalculate the view matrix for this [Camera] with it's local parameters.
     */
    fun update() {
        viewMatrix.lookAt(position, target, up)
    }

    /**
     * Move the camera by the position deltas given relative to it's current orientation.
     */
    fun move(dx: Float, dy: Float, dz: Float) {
        position.add(right.mul(dx))
        position.add(up.mul(dy))
        position.add(forward.mul(dz))

        update()
    }

    companion object {
        val WORLD_RIGHT = Vector3f(1f, 0f, 0f)
        val WORLD_UP = Vector3f(0f, 1f, 0f)
        val WORLD_FORWARD = Vector3f(0f, 0f, 1f)
    }
}
