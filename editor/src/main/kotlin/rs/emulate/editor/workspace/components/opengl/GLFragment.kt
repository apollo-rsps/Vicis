package rs.emulate.editor.workspace.components.opengl

import io.reactivex.Observable.merge
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.WritableImage
import rs.emulate.editor.workspace.components.opengl.render.WritableImageRenderSurface
import tornadofx.*
import java.lang.Integer.max
import java.util.concurrent.TimeUnit

class GLFragment : Fragment() {
    val surfaceImageProperty = SimpleObjectProperty<WritableImage>(WritableImage(1, 600))
    var surfaceImage by surfaceImageProperty

    lateinit var surface: WritableImageRenderSurface
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
        surface.stop()
    }

    override fun onDock() {
        val height = max(root.height.toInt(), 1)
        val width = max(root.width.toInt(), 1)

        surfaceImage = WritableImage(width, height)
        surface = WritableImageRenderSurface.create(surfaceImage)

        surfaceResizeSubscription = merge(changesOf(root.widthProperty()), changesOf(root.heightProperty()))
            .debounce(100, TimeUnit.MILLISECONDS)
            .subscribe {
                val newHeight = max(root.height.toInt(), 1)
                val newWidth = max(root.width.toInt(), 1)

                surfaceImage = WritableImage(newWidth, newHeight)
                surface.setTarget(surfaceImage)
            }

        surface.start()
    }

}
