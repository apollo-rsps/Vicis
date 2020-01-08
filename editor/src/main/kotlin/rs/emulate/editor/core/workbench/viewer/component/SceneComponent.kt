package rs.emulate.editor.core.workbench.viewer.component

import io.reactivex.Observable.merge
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import io.reactivex.rxjavafx.observables.JavaFxObservable.valuesOf
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import rs.emulate.scene3d.Node
import rs.emulate.scene3d.Scene
import rs.emulate.scene3d.SceneAnimator
import rs.emulate.scene3d.backend.opengl.OpenGLRenderer
import rs.emulate.scene3d.backend.opengl.target.javafx.JavaFXRenderTarget
import java.lang.Integer.max
import java.util.concurrent.TimeUnit

class SceneComponent : Pane() {
    private val scene3d = Scene()
    val activeProperty = SimpleBooleanProperty(true)

    private val onSizeChanged = merge(changesOf(heightProperty()), changesOf(widthProperty())).throttleLast(200L, TimeUnit.MILLISECONDS)
    private val onVisibilityChanged = valuesOf(activeProperty).distinctUntilChanged()

    private val onDisposed = changesOf(sceneProperty()).filter { it.newVal == null }
    private val onHidden = onVisibilityChanged.filter { visible -> !visible }
    private val onShown = onVisibilityChanged.filter { visible -> visible }
    private val onStarted = changesOf(sceneProperty()).filter { it.newVal != null }.distinct()

    private var sceneAnimator: SceneAnimator

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

        val frontBuffer = ImageView()
        frontBuffer.fitWidth = -1.0
        frontBuffer.fitHeight = -1.0

        onSizeChanged.subscribe {
            val newWidth = max(width.toInt(), 1)
            val newHeight = max(height.toInt(), 1)

            scene3d.width = newWidth
            scene3d.height = newHeight
            scene3d.camera.perspective(newWidth, newHeight, 45f, 0.01f, 100f)
        }

        scene3d.camera.move(0f, 1f, -5f)

        val rendererTarget = JavaFXRenderTarget(frontBuffer)
        val renderer = OpenGLRenderer()

        sceneAnimator = SceneAnimator(scene3d, renderer, rendererTarget)

        onHidden.subscribe { sceneAnimator.pause() }
        onShown.subscribe { sceneAnimator.resume() }
        onDisposed.subscribe { sceneAnimator.stop() }
        onStarted.subscribe { sceneAnimator.start() }

        children.add(frontBuffer)
    }

    fun addChild(child: Node) {
        scene3d.addChild(child)
    }
}
