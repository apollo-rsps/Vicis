package rs.emulate.scene3d.backend

import rs.emulate.scene3d.Scene

/**
 * A renderer for a 3D [Scene].
 */
abstract class Renderer(protected val scene: Scene) {

    /**
     * A flag indicating if the [Renderer] has finished initializing the display.
     */
    abstract var initialized: Boolean

    /**
     * Initialize the display surface used to render.
     */
    abstract fun initialize()

    /**
     * Resize the dimensions of the [Renderer]s display.
     */
    abstract fun resize(width: Int, height: Int)

    /**
     * Render the geometry of the scene.  Called every frame.
     */
    abstract fun render()

    /**
     * Stop the renderer and begin disposing of any resources allocated.
     */
    abstract fun stop()
}
