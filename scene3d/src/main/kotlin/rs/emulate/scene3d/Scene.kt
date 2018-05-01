package rs.emulate.scene3d

import java.util.HashSet
import kotlin.collections.ArrayList

/**
 * Root node of the scene-graph.
 */
class Scene() : Node() {
    override var dirty: Boolean = false
    var width: Int = 1
    var height: Int = 1
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
