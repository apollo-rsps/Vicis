package rs.emulate.scene3d

import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import rs.emulate.scene3d.material.Material
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.locks.ReentrantLock
import kotlin.properties.Delegates.observable

abstract class Node {

    /**
     * An internal map used to store metadata that can be used by the
     * renderer.
     */
    internal val metadata = mutableMapOf<String, Any>()

    /**
     * A flag indicating if this [Node] is dirty and needs its buffers
     * re-uploaded to the GPU.
     */
    abstract var dirty: Boolean

    /**
     * The [Material] this [Node] is rendered with.
     */
    var material: Material = Material()

    /**
     * The [Node]'s local model matrix.
     */
    val modelMatrix: Matrix4f = Matrix4f().identity()

    /**
     * The [Node]'s world model matrix.
     */
    val worldMatrix: Matrix4f = Matrix4f().identity()

    /**
     * The local translation of this [Node].
     */
    var position by observable(Vector3f(0f, 0f, 0f)) { _, _, _ -> recomposeModelMatrix() }

    /**
     * The local translation of this [Node].
     */
    var rotation by observable(Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)) { _, _, _ -> recomposeModelMatrix() }

    /**
     * The local scale of this [Node].
     */
    var scale by observable(Vector3f(0.1f, 0.1f, 0.1f)) { _, _, _ -> recomposeModelMatrix() }

    /**
     * The parent [Node], used to calculate the world matrix.
     */
    var parent: Node? = null

    /**
     * A list of children [Node]s relative to this [Node].
     */
    var children = CopyOnWriteArrayList<Node>()

    /**
     * A write lock protecting access to this [Node] during updates.
     */
    val lock = ReentrantLock()

    /**
     * Add a child to this [Node] and update it's parent field.
     */
    fun addChild(child: Node) {
        child.parent = this
        children.add(child)
    }

    /**
     * Remove a child from this [Node] and set it's parent field to `null`.
     */
    fun removeChild(child: Node) {
        child.parent = null
        children.remove(child)
    }

    /**
     * Run any updates for this [Node] and recalculate it's [worldMatrix] based on it's parent.
     */
    fun update() {
        val parent = this.parent

        if (parent != null) {
            worldMatrix.set(parent.worldMatrix)
            worldMatrix.mul(modelMatrix)
        } else {
            worldMatrix.set(modelMatrix)
        }
    }

    /**
     * Recompute this [Node]'s model matrix after a change to any of its components.
     */
    private fun recomposeModelMatrix() {
        modelMatrix.set(IDENTITY_MATRIX)
        modelMatrix.translate(position)
        modelMatrix.rotate(rotation)
        modelMatrix.scale(scale)
    }

    companion object {
        val IDENTITY_MATRIX = Matrix4f().identity()
    }
}
