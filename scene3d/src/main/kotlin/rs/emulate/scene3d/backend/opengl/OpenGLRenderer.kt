package rs.emulate.scene3d.backend.opengl

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import rs.emulate.scene3d.Geometry
import rs.emulate.scene3d.Node
import rs.emulate.scene3d.Scene
import rs.emulate.scene3d.backend.RenderTarget
import rs.emulate.scene3d.backend.Renderer
import rs.emulate.scene3d.backend.opengl.target.OpenGLRenderTarget

val Node.glState: OpenGLGeometryState
    get() = metadata.computeIfAbsent(OpenGLRenderer.GL_METADATA_KEY) { OpenGLGeometryState() } as OpenGLGeometryState

/**
 * A scene renderer that uses modern OpenGL to render the scene.
 *
 * @param scene The scene to render.
 * @param target The render target to blit the scene to.
 */
class OpenGLRenderer(val visible: Boolean = false) : Renderer {

    override fun render(scene: Scene, target: RenderTarget, alpha: Float) {
        target.bind()

        glClearColor(0f, 0.0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT)
        glViewport(0, 0, scene.width, scene.height)

        val geometryNodes = scene.discover { it is Geometry }
        for (node in geometryNodes) {
            val geometry = node as Geometry
            val glState = geometry.glState

            if (geometry.dirty && !geometry.lock.isLocked) {
                if (!glState.initialized) {
                    glState.initialize(geometry)
                } else {
                    glState.update(geometry)
                }
            } else {
                glState.draw(scene.camera, geometry)
            }
        }

        target.blit()
    }

    override fun dispose() {
        // @todo - find all `Geometry` nodes and deallocate their resources
    }

    companion object {
        const val GL_METADATA_KEY = "OpenGLState"
    }
}
