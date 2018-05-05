package rs.emulate.scene3d

import java.util.HashSet
import kotlin.collections.ArrayList
import kotlin.properties.Delegates.observable

/**
 * Root node of the scene-graph.
 */
class Scene : Node() {
    override var  dirty: Boolean = true

    /**
     * The width, in pixels, of the scenes viewport.
     */
    var width: Int by observable(1) { _, _, _ -> dirty = true }

    /**
     * The height, in pixels, of the scenes viewport.
     */
    var height: Int by observable(1) { _, _, _ -> dirty = true }

    val camera = Camera()

    fun discover(func: (Node) -> Boolean): ArrayList<Node> {
        val visited = HashSet<Node>()
        val matched = ArrayList<Node>()

        fun discover(current: Node, f: (Node) -> Boolean) {
            if (!visited.add(current)) return
            for (v in current.children) {
                if (f(v)) {
                    matched.add(v)
                }

                discover(v, f)
            }
        }

        discover(this as Node, func)

        return matched
    }

}
