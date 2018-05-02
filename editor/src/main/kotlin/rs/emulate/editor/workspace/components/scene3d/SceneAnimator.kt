package rs.emulate.editor.workspace.components.scene3d

import rs.emulate.scene3d.backend.Renderer

class SceneAnimator(val parent: Thread, val renderer: Renderer) : Thread() {
    var stopped: Boolean = false

    override fun run() {
        renderer.initialize()

        while (parent.isAlive && !stopped) {
            renderer.render()
            Thread.sleep(1000L / 60L) //@todo - more robust
        }
    }
}
