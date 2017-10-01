package rs.emulate.editor.ui.scene3d

import com.jme3.math.ColorRGBA
import com.jme3x.jfx.injfx.JmeForImageView
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority
import tornadofx.*

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

                    val rootNode = jmeApp.rootNode
                    val assetManager = jmeApp.assetManager
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
}