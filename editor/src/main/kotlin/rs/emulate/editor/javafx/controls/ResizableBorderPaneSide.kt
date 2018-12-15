package rs.emulate.editor.javafx.controls

import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Side
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.Control
import javafx.scene.layout.Region
import rs.emulate.editor.utils.javafx.onChange

class ResizableBorderPaneSide(val side: Side, initial: Property<out Node>) {

    private var initialized = false

    /**
     * A flag indicating if this side can be resized.  Controls whether the resize handle is made present.
     */
    val resizableProperty = SimpleBooleanProperty(true)
    var resizable : Boolean
        get() = resizableProperty.get()
        set(value) {
            resizableProperty.set(value)
        }

    /**
     * The [Node] that is the target of drag events, may be different from the node being resized.
     */
    val dragTargetProperty = SimpleObjectProperty<Node>()
    var dragTarget : Node
        get() = dragTargetProperty.get()
        set(value) {
            dragTargetProperty.set(value)
        }

    /**
     * The [Node] that receives resize events.
     */
    val resizeTargetProperty = SimpleObjectProperty<Node>()
    var resizeTarget : Node?
        get() = resizeTargetProperty.get()
        set(value) {
            resizeTargetProperty.set(value)
        }

    init {
        initial.onChange { node ->
            if (!initialized) {
                dragTarget = node

                if (resizeTarget == null) {
                    resizeTarget = node
                }

                initialized = true
            }
        }
    }

    /**
     * Check if the coordinates given by [x] and [y] are within the resize box of this side.
     */
    fun isPointInResizeBounds(x: Double, y: Double): Boolean {
        val node = dragTarget ?: return false

        val layoutBounds = node.layoutBounds
        val resizeAnchorSize = RESIZE_ANCHOR_SIZE

        val width = layoutBounds.width
        val height = layoutBounds.height
        val layoutX = node.layoutX
        val layoutY = node.layoutY

        val (x1, x2) = when (side) {
            Side.LEFT -> centered(layoutX + width, resizeAnchorSize)
            Side.RIGHT -> centered(layoutX, resizeAnchorSize)
            else -> layoutX to layoutX + width
        }

        val (y1, y2) = when (side) {
            Side.TOP -> centered(layoutY + height, resizeAnchorSize)
            Side.BOTTOM -> centered(layoutY, resizeAnchorSize)
            else -> layoutY to layoutY + height
        }

        return x in x1..x2 && y in y1..y2
    }

    /**
     * Gets the _resize_ cursor for the this side.
     */
    fun resizeCursor() = when (side) {
        Side.LEFT, Side.RIGHT -> Cursor.H_RESIZE
        Side.TOP, Side.BOTTOM -> Cursor.V_RESIZE
        else -> Cursor.DEFAULT
    }

    /**
     * Set the preferred width of the [resizeTarget] this side represents.
     */
    fun setPrefWidth(width: Double) {
        val node = resizeTarget
        val resizeBounds = resizeTarget?.layoutBounds ?: dragTarget.layoutBounds
        val dragBounds = dragTarget.layoutBounds
        val padding = dragBounds.width - resizeBounds.width
        val newWidth = width - padding

        when (node) {
            is Control -> node.prefWidth = newWidth
            is Region -> node.prefWidth = newWidth
            else -> {
                if (node != null) {
                    println("Unrecognized type: ${node::class.simpleName}")
                }
            }
        }
    }

    /**
     * Set the preferred height of the [resizeTarget] this side represents.
     */
    fun setPrefHeight(height: Double) {
        val node = resizeTarget
        val resizeBounds = resizeTarget?.layoutBounds ?: dragTarget.layoutBounds
        val dragBounds = dragTarget.layoutBounds
        val padding = dragBounds.height - resizeBounds.height
        val newHeight = height - padding

        when (node) {
            is Control -> node.prefHeight = newHeight
            is Region -> node.prefHeight = newHeight
            else -> {
                if (node != null) {
                    println("Unrecognized type: ${node::class.simpleName}")
                }
            }
        }
    }

    companion object {
        /**
         * Create a 1D line centered on the given [point] with the specified [length].
         */
        private fun centered(point: Double, length: Double) = (point - length / 2.0) to (point + length / 2.0)

        const val RESIZE_ANCHOR_SIZE = 7.0 // 7 pixels
    }
}
