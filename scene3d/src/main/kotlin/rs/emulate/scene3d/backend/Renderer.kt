package rs.emulate.scene3d.backend

import rs.emulate.scene3d.Scene

/**
 * A renderer for a 3D [Scene].
 */
interface Renderer {
    /**
     * Render the geometry of a [Scene] to a [RenderTarget].  Called every frame.
     *
     * @param scene The scene to render.
     * @param target The [RenderTarget] that the [Renderer] should write its pixels to.
     * @param alpha An optional value between [0,1) representing the time until the next [Scene] update.
     */
    fun render(scene: Scene, target: RenderTarget, alpha: Float = 0.0f)

    /**
     * Begin disposing of any resources allocated by the [Renderer]..
     */
    fun dispose()
}
