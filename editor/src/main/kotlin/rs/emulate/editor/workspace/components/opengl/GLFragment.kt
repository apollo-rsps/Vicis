package rs.emulate.editor.workspace.components.opengl

import io.reactivex.Observable.merge
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import io.reactivex.rxjavafx.observables.JavaFxObservable.eventsOf
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.WritableImage
import javafx.stage.WindowEvent
import rs.emulate.editor.workspace.components.opengl.render.FboRenderTarget
import rs.emulate.editor.workspace.components.opengl.render.Renderer
import rs.emulate.editor.workspace.components.opengl.render.RendererEventListener
import tornadofx.*
import java.lang.Integer.max
import java.util.concurrent.TimeUnit

class GLFragment : Fragment() {
    val rendererListener: RendererEventListener by param()

    val surfaceImageProperty = SimpleObjectProperty<WritableImage>(WritableImage(1, 600))
    var rendererBufferImage by surfaceImageProperty

    val rendererTarget = FboRenderTarget(rendererBufferImage)

    lateinit var renderer: Renderer
    lateinit var surfaceResizeSubscription: Disposable

    override val root = hbox {
        imageview {
            imageProperty().bind(surfaceImageProperty)
            fitWidthProperty().bind(this@hbox.widthProperty())
            fitHeightProperty().bind(this@hbox.heightProperty())

            // Allow the node to manage its own layout
            isManaged = false
        }
    }

    override fun onUndock() {
        surfaceResizeSubscription.dispose()
        renderer.stop()
    }

    override fun onDock() {
        renderer = Renderer.create(rendererTarget, rendererListener)
        surfaceResizeSubscription = merge(changesOf(root.widthProperty()), changesOf(root.heightProperty()))
            .throttleLast(100, TimeUnit.MILLISECONDS)
            .subscribe {
                val newWidth = max(root.width.toInt(), 1)
                val newHeight = max(root.height.toInt(), 1)

                renderer.runLater {
                    rendererBufferImage = WritableImage(newWidth, newHeight)
                    rendererTarget.retarget(rendererBufferImage)
                }

                renderer.resize(newWidth, newHeight)
            }

        renderer.start()
    }
}
