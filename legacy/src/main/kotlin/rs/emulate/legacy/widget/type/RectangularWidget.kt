package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.RECTANGULAR
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.shared.util.putBoolean
import java.nio.ByteBuffer

/**
 * A [RECTANGULAR][WidgetGroup.RECTANGULAR] [Widget].
 *
 * @param id The id of the RectangleWidget.
 * @param parent The parent id of the RectangleWidget.
 * @param optionType The [WidgetOption] of the RectangleWidget.
 * @param content The content type of the RectangleWidget.
 * @param width The width of the RectangleWidget, in pixels.
 * @param height The width of the RectangleWidget, in pixels.
 * @param alpha The alpha of the RectangleWidget, in pixels.
 * @param hoverId The hover id of the RectangleWidget, as an Int?.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the RectangularWidget.
 * @param hoverText The hover text of the RectangularWidget.
 * @param filled Whether or not the RectangularWidget is drawn filled.
 * @param colour The [ColourPair] containing the default and secondary colours of the rectangle.
 * @param hover The ColourPair containing the default and secondary hover colours.
 */
class RectangularWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hoverId: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    var filled: Boolean,
    var colour: ColourPair,
    var hover: ColourPair
) : Widget(id, parent, RECTANGULAR, optionType, content, width, height, alpha, hoverId, scripts, option, hoverText) {

    override fun encodeBespoke(): ByteBuffer {
        val buffer = ByteBuffer.allocate(4 * Integer.BYTES + java.lang.Byte.BYTES)
        buffer.putBoolean(filled)

        for (pair in listOf(colour, hover)) {
            buffer.putInt(pair.default)
            buffer.putInt(pair.secondary)
        }

        return buffer.apply { flip() }
    }

}
