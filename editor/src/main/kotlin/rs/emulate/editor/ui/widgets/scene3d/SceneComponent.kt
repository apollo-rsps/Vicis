package rs.emulate.editor.ui.widgets.scene3d

import io.reactivex.Observable.merge
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import io.reactivex.rxjavafx.observables.JavaFxObservable.valuesOf
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import rs.emulate.editor.utils.reactivex.nullableChangesOf
import rs.emulate.scene3d.Scene
import rs.emulate.scene3d.SceneAnimator
import rs.emulate.scene3d.backend.opengl.OpenGLRenderer
import rs.emulate.editor.ui.widgets.scene3d.javafx.JavaFXRenderTarget
import tornadofx.*
import java.lang.Integer.max
import java.util.concurrent.TimeUnit

class SceneComponent : Pane() {
    val scene3d = Scene()

    val activeProperty = SimpleBooleanProperty(true)
    var active by activeProperty

    private val onSizeChanged = merge(changesOf(heightProperty()), changesOf(widthProperty())).throttleLast(200L, TimeUnit.MILLISECONDS)
    private val onVisibilityChanged = valuesOf(activeProperty).distinctUntilChanged()

    private val onDisposed = nullableChangesOf(sceneProperty()).filter { it.newVal == null }
    private val onHidden = onVisibilityChanged.filter { visible -> !visible }
    private val onShown = onVisibilityChanged.filter { visible -> visible }
    private val onStarted = nullableChangesOf(sceneProperty()).filter { it.newVal != null }.distinct()

    var sceneAnimator: SceneAnimator

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

        val imageview = imageview {
            style {
                backgroundColor += Color.BLACK
            }

            fitWidth = -1.0
            fitHeight = -1.0
        }

        onSizeChanged.subscribe {
            val newWidth = max(width.toInt(), 1)
            val newHeight = max(height.toInt(), 1)

            scene3d.width = newWidth
            scene3d.height = newHeight
            scene3d.camera.perspective(newWidth, newHeight, 45f, 0.01f, 100f)
        }

        scene3d.camera.move(0f, 1f, -5f)

        val rendererTarget = JavaFXRenderTarget(imageview)
        val renderer = OpenGLRenderer()

        sceneAnimator = SceneAnimator(scene3d, renderer, rendererTarget)

        onHidden.subscribe { sceneAnimator.pause() }
        onShown.subscribe { sceneAnimator.resume() }
        onDisposed.subscribe { sceneAnimator.stop() }
        onStarted.subscribe { sceneAnimator.start() }

        add(imageview)
    }
}
