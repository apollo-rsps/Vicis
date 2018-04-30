package rs.emulate.editor.workspace.components.opengl

import io.reactivex.Observable.merge
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.WritableImage
import javafx.scene.layout.HBox
import rs.emulate.editor.workspace.components.opengl.render.FboRenderTarget
import rs.emulate.editor.workspace.components.opengl.render.Renderer
import rs.emulate.editor.workspace.components.opengl.render.RendererEventListener
import tornadofx.*
import java.lang.Integer.max
import java.util.concurrent.TimeUnit

class GLFragment(val rendererListener: RendererEventListener) : HBox() {
    val surfaceImageProperty = SimpleObjectProperty<WritableImage>(WritableImage(1, 600))
    var rendererBufferImage by surfaceImageProperty

    val rendererTarget = FboRenderTarget(rendererBufferImage)

    lateinit var renderer: Renderer
    lateinit var surfaceResizeSubscription: Disposable

    fun onUndock() {
        surfaceResizeSubscription.dispose()
        renderer.stop()
    }

    fun onDock() {
        renderer = Renderer.create(rendererTarget, rendererListener)
        surfaceResizeSubscription = merge(changesOf(widthProperty()), changesOf(heightProperty()))
            .throttleLast(100, TimeUnit.MILLISECONDS)
            .subscribe {
                val newWidth = max(width.toInt(), 1)
                val newHeight = max(height.toInt(), 1)

                renderer.runLater {
                    rendererBufferImage = WritableImage(newWidth, newHeight)
                    rendererTarget.retarget(rendererBufferImage)
                }

                renderer.resize(newWidth, newHeight)
            }

        renderer.start()
    }

    init {
        add(imageview {
            imageProperty().bind(surfaceImageProperty)
            fitWidthProperty().bind(widthProperty())
            fitHeightProperty().bind(heightProperty())

            // Allow the node to manage its own layout
            isManaged = false
        })
    }
}
