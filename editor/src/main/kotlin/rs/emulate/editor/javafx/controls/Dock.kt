package rs.emulate.editor.javafx.controls

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.Group
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToolBar
import javafx.scene.layout.*
import rs.emulate.editor.utils.javafx.doubleBinding
import rs.emulate.editor.utils.javafx.mapped
import rs.emulate.editor.utils.javafx.objectBinding
import rs.emulate.editor.utils.javafx.onChange
import java.util.concurrent.Callable

class Dock : BorderPane() {
    val toolbar = ToolBar()

    val contentAreaProperty = SimpleObjectProperty<Pane>()
    val contentGroup = Group()
    private val items = FXCollections.observableArrayList<TitledPane>()

    val sideProperty = SimpleObjectProperty<Side>()
    var side: Side
        get() = sideProperty.get()
        set(value) {
            sideProperty.set(value)
        }

    init {
        toolbar.styleClass += "dock-bar"
        toolbar.orientationProperty().bind(sideProperty.objectBinding {
            when (it) {
                Side.TOP, Side.BOTTOM -> Orientation.HORIZONTAL
                else -> Orientation.VERTICAL
            }
        })

        leftProperty().bind(placementBinding(Side.LEFT))
        rightProperty().bind(placementBinding(Side.RIGHT))
        topProperty().bind(placementBinding(Side.TOP))
        bottomProperty().bind(placementBinding(Side.BOTTOM))
        centerProperty().bind(contentAreaProperty)

        val isExpanded = Bindings.createBooleanBinding(Callable { items.any { it.isVisible } }, items)

        contentAreaProperty.bind(sideProperty.objectBinding { side ->
            val pane = when (side) {
                Side.BOTTOM, Side.TOP -> HBox().also { minWidth = 0.0 }
                else -> VBox().also { minHeight = 0.0 }
            }

            pane.managedProperty().bind(isExpanded)
            pane.visibleProperty().bind(isExpanded)

            Bindings.bindContent(pane.children, items)

            pane
        })

        Bindings.bindContent(toolbar.items, items.mapped { item ->
            val button = ToggleButton()
            button.textProperty().bind(item.textProperty())
            button.selectedProperty().bindBidirectional(item.visibleProperty())
            button.rotateProperty().bind(sideProperty.doubleBinding {
                when (it) {
                    Side.LEFT -> -90.0
                    Side.RIGHT -> 90.0
                    else -> 0.0
                }
            })

            // Exclude the dock item from layout calculations if it isn't visible.
            item.managedProperty().bind(item.visibleProperty())

            Group(button)
        })

        sideProperty.onChange {
            styleClass.clear()
            styleClass.addAll("dock", it.name.toLowerCase())
        }
    }

    fun addItem(item: TitledPane) {
        VBox.setVgrow(item, Priority.ALWAYS)
        HBox.setHgrow(item, Priority.ALWAYS)
        item.isCollapsible = false

        items += item
    }

    private fun placementBinding(side: Side) = sideProperty.objectBinding {
        if (side == it) {
            toolbar
        } else {
            null
        }
    }
}
