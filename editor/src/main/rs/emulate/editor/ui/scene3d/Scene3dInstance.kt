package rs.emulate.editor.ui.scene3d

import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer
import com.jme3.util.BufferUtils
import com.jme3x.jfx.injfx.JmeForImageView
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority
import tornadofx.*


data class IndexedTriangle(val a: Int, val b: Int, val c: Int)

object Scene3dInstance : View("Scene3dInstance") {
    override val root = vbox()

    val jme = JmeForImageView()

    init {
        with(root) {
            style {
                vgrow = Priority.ALWAYS
                backgroundColor += c("black")
            }

            val canvas = ImageView()
            add(canvas)

            Platform.runLater {

                jme.bind(canvas)
                jme.enqueue { jmeApp ->
                    jmeApp.stateManager.attach(Scene3dCameraState());
                    jmeApp.viewPort.backgroundColor = ColorRGBA.Black

                    val cameraInputListener = Scene3dCameraInputListener()
                    cameraInputListener.jme = jmeApp
                    cameraInputListener.speed = 1.0f
                    Scene3dCameraInputListener.bindDefaults(canvas, cameraInputListener)
                }
            }

            canvas.fitHeightProperty().bind(heightProperty())
            canvas.fitWidthProperty().bind(widthProperty())
            canvas.isPreserveRatio = false
        }
    }


    fun reset() {
        jme.enqueue {
            it.rootNode.detachAllChildren()
        }
    }

    fun showNewModel(vertices: Array<Vector3f>, indices: IntArray) {
        jme.enqueue { jmeApp ->

            val mesh = Mesh()
            mesh.mode = Mesh.Mode.Triangles
            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(*vertices))
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(*indices))
            mesh.updateBound()

            val rootNode = jmeApp.rootNode
            val assetManager = jmeApp.assetManager

            val mat = Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
            mat.additionalRenderState.isWireframe = true
            mat.additionalRenderState.lineWidth = 2.0f

            val geo = Geometry("Scene3dMesh", mesh)
            geo.setLocalTranslation(0f, 0f, 0f)
            geo.setLocalScale(0.01f)
            geo.rotate(0f, 0f, Math.toRadians(180.0).toFloat())
            geo.material = mat

            rootNode.detachChildNamed("Scene3dMesh")
            rootNode.attachChild(geo)
        }
    }
}