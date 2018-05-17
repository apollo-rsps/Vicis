package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.ITEM_LIST
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.Point

/**
 * A [WidgetGroup.ITEM_LIST] [Widget].
 *
 * @param id The id of the ItemListWidget.
 * @param parent The parent id of the ItemListWidget.
 * @param optionType The [WidgetOption] of the ItemListWidget.
 * @param content The content type of the ItemListWidget.
 * @param width The width of the ItemListWidget, in pixels.
 * @param height The width of the ItemListWidget, in pixels.
 * @param alpha The alpha of the ItemListWidget, in pixels.
 * @param hover The hover id of the ItemListWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the ItemListWidget.
 * @param hoverText The hover text of the ItemListWidget.
 * @param centered Whether or not the text is centered.
 * @param colour The [ColourPair] containing the default and secondary colours of the text.
 * @param font The id of the font used to draw the text with.
 * @param shadowed Whether or not text is drawn with shadowing.
 * @param actions The [List] of menu actions.
 * @param padding The sprite padding.
 */
class ItemListWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hover: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val centered: Boolean,
    private val colour: Int,
    private val font: Int,
    private val shadowed: Boolean,
    private val actions: List<String>,
    private val padding: Point
) : Widget(id, parent, ITEM_LIST, optionType, content, width, height, alpha, hover, scripts, option, hoverText) {

    override fun encodeBespoke(): DataBuffer {
        val size = actions.map(String::length).sum() + actions.size

        val action = DataBuffer.allocate(size)
        actions.forEach { action.putAsciiString(it) }
        action.flip()

        val buffer = DataBuffer.allocate(
            4 * java.lang.Byte.BYTES + 2 * java.lang.Short.BYTES + Integer.BYTES + action.remaining()
        )

        buffer.putBoolean(centered)
        buffer.putByte(font)
        buffer.putBoolean(shadowed)

        buffer.putInt(colour)
        buffer.putShort(padding.x).putShort(padding.y)

        buffer.putBoolean(!actions.isEmpty())
        buffer.put(action)

        return buffer.flip().asReadOnlyBuffer()
    }

}
