package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.CONTAINER
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.Point

/**
 * Contains properties used by [WidgetGroup.CONTAINER] [Widget]s.
 *
 * @param id The id of the ContainerWidget.
 * @param parent The parent id of the ContainerWidget.
 * @param optionType The [WidgetOption] of the ContainerWidget.
 * @param content The content type of the ContainerWidget.
 * @param width The width of the ContainerWidget, in pixels.
 * @param height The width of the ContainerWidget, in pixels.
 * @param alpha The alpha of the ContainerWidget, in pixels.
 * @param hover The hover id of the ContainerWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the ContainerWidget.
 * @param hoverText The hover text of the ContainerWidget.
 * @param scrollLimit The scroll limit of the container ContainerWidget.
 * @param hidden Whether or not the ContainerWidget is hidden.
 * @param children The child ids of the ContainerWidget.
 * @param childPoints The [Point]s of the child ContainerWidgets.
 */
class ContainerWidget( // TODO this constructor size is PTSD-inducing
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hover: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val scrollLimit: Int,
    private val hidden: Boolean,
    children: IntArray,
    childPoints: List<Point>
) : Widget(id, parent, CONTAINER, optionType, content, width, height, alpha, hover, scripts, option, hoverText) {

    /**
     * The [Point]s of the child Widgets.
     */
    private val childPoints: List<Point> = childPoints.toList()

    /**
     * The child ids of the Widget.
     */
    private val children: IntArray = children.clone()

    init {
        require(children.size == childPoints.size) { "Child ids and child points must be of equal length." }
    }

    public override fun encodeBespoke(): DataBuffer {
        val buffer = DataBuffer.allocate((2 + children.size) * java.lang.Short.BYTES + java.lang.Byte.BYTES)
        buffer.putShort(scrollLimit)
        buffer.putBoolean(hidden)

        val size = children.size
        buffer.putShort(size)

        for (index in 0 until size) {
            buffer.putShort(children[index])

            val point = childPoints[index]
            buffer.putShort(point.x)
            buffer.putShort(point.y)
        }

        return buffer.flip().asReadOnlyBuffer()
    }

}
