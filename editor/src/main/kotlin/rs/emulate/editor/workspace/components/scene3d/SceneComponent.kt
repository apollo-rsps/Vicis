package rs.emulate.editor.workspace.components.scene3d

import glm_.vec3.Vec3
import io.reactivex.Observable.merge
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import rs.emulate.scene3d.Mesh
import rs.emulate.scene3d.Scene
import rs.emulate.scene3d.backend.opengl.OpenGLRenderer
import rs.emulate.scene3d.backend.opengl.target.javafx.JavaFXRenderTarget
import tornadofx.*
import java.lang.Integer.max

class SceneComponent : HBox() {
    val surfaceImageProperty = SimpleObjectProperty<WritableImage>(WritableImage(1, 600))
    var rendererBufferImage by surfaceImageProperty

    val scene3d = Scene()
    val rendererTarget = JavaFXRenderTarget(rendererBufferImage)
    val renderer = OpenGLRenderer(scene3d, rendererTarget, false)

    lateinit var sceneAnimator: SceneAnimator
    lateinit var surfaceResizeSubscription: Disposable

    fun onUndock() {
        surfaceResizeSubscription.dispose()
        sceneAnimator.stopped = true
    }

    fun onDock() {
        surfaceResizeSubscription = merge(changesOf(widthProperty()), changesOf(heightProperty()))
            .subscribe {
                val newWidth = max(width.toInt(), 1)
                val newHeight = max(height.toInt(), 1)
                scene3d.camera.perspective(newWidth, newHeight, 45f, 0.01f, 100f)
                scene3d.width = newWidth
                scene3d.height = newHeight

                rendererBufferImage = WritableImage(newWidth, newHeight)
                rendererTarget.target = rendererBufferImage
                renderer.resize(newWidth, newHeight)
            }

        scene3d.camera.move(0f, 1f, -5f)
        sceneAnimator = SceneAnimator(Thread.currentThread(), renderer)
        sceneAnimator.start()
    }

    init {
        isFocusTraversable = true

        setOnKeyPressed {
            when (it.code) {
                KeyCode.W, KeyCode.UP -> scene3d.camera.move(0f, 0f, 1f)
                KeyCode.A, KeyCode.LEFT -> scene3d.camera.move(-1f, 0f, 0f)
                KeyCode.S, KeyCode.DOWN -> scene3d.camera.move(0f, 0f, -1f)
                KeyCode.D, KeyCode.RIGHT -> scene3d.camera.move(1f, 0f, 0f)
            }
        }

        add(imageview {
            imageProperty().bind(surfaceImageProperty)
            fitWidthProperty().bind(widthProperty())
            fitHeightProperty().bind(heightProperty())

            style {
                backgroundColor += Color.BLACK
            }

            // Allow the node to manage its own layout
            isManaged = false

            // Flip the render buffer image so that 0,0 is at the top left instead of
            // bottom left
            scaleX = 1.0
            scaleY = -1.0
        })
    }
}
