package rs.emulate.editor.ui.widgets.layout

import filteredEvents
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.withLatestFrom
import javafx.geometry.Side
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.lang.Math.abs
import java.lang.Math.max

/**
 * A border pane that allows its children to be resized by dragging on handles present at
 * their borders.
 */
class ResizableBorderPane : BorderPane() {
    val leftSide = ResizableBorderPaneSide(Side.LEFT, leftProperty())
    val rightSide = ResizableBorderPaneSide(Side.RIGHT, rightProperty())
    val topSide = ResizableBorderPaneSide(Side.TOP, topProperty())
    val bottomSide = ResizableBorderPaneSide(Side.BOTTOM, bottomProperty())

    private val sides = listOf(leftSide, rightSide, topSide, bottomSide)

    private val mouseOverSubscription: Disposable
    private val resizeSubscription: Disposable

    private fun findSideAtResizePoint(x: Double, y: Double): ResizableBorderPaneSide? {
        return sides.find { side -> side.resizable && side.isPointInResizeBounds(x, y) }
    }

    init {
        leftProperty().onChange { node -> node?.addClass("left") }
        rightProperty().onChange { node -> node?.addClass("right") }
        topProperty().onChange { node -> node?.addClass("top") }
        bottomProperty().onChange { node -> node?.addClass("bottom") }

        val mouseOver = filteredEvents(MouseEvent.MOUSE_MOVED)
        val mouseDrag = filteredEvents(MouseEvent.MOUSE_DRAGGED)
        val mouseDown = filteredEvents(MouseEvent.MOUSE_PRESSED)
        val mouseUp = filteredEvents(MouseEvent.MOUSE_RELEASED)

        mouseOverSubscription = mouseOver
            .subscribe {
                val borderSide = findSideAtResizePoint(it.x, it.y)
                val cursor = if (borderSide == null) Cursor.DEFAULT else borderSide.resizeCursor()

                setCursor(cursor)
            }

        resizeSubscription = mouseDown
            .concatMap {
                val borderSide = findSideAtResizePoint(it.x, it.y)
                val observable = if (borderSide == null) {
                    empty()
                } else {
                    it.consume()
                    just(borderSide)
                }

                mouseDrag.takeUntil(mouseUp).withLatestFrom(observable) { event, n -> Pair(n, event) }
            }
            .subscribe { it ->
                val borderSide = it.first
                val event = it.second

                if (event == null || borderSide == null) {
                    return@subscribe
                }

                event.consume()

                val node = borderSide.dragTarget
                val nodeBounds = borderSide.dragTarget.layoutBounds

                val newWidth = event.x - node.layoutX - if (borderSide.side == Side.RIGHT) nodeBounds.width else 0.0
                val newHeight = event.y - node.layoutY - if (borderSide.side == Side.BOTTOM) nodeBounds.height else 0.0

                when (borderSide.side) {
                    Side.LEFT, Side.RIGHT -> borderSide.setPrefWidth(max(0.0, abs(newWidth)))
                    Side.TOP, Side.BOTTOM -> borderSide.setPrefHeight(max(0.0, abs(newHeight)))
                }
            }
    }
}
