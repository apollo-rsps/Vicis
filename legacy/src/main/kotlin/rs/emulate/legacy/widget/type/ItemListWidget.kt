package rs.emulate.legacy.widget.type

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.ITEM_LIST
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.util.Point
import rs.emulate.util.writeAsciiString

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

    override fun encodeBespoke(): ByteBuf {
        val size = actions.map(String::length).sum() + actions.size

        val action = Unpooled.buffer(size)
        actions.forEach { action.writeAsciiString(it) }

        val buffer = Unpooled.buffer(
            4 * java.lang.Byte.BYTES + 2 * java.lang.Short.BYTES + Integer.BYTES + action.readableBytes()
        )

        buffer.writeBoolean(centered)
        buffer.writeByte(font)
        buffer.writeBoolean(shadowed)

        buffer.writeInt(colour)
        buffer.writeShort(padding.x).writeShort(padding.y)

        buffer.writeBoolean(!actions.isEmpty())
        buffer.writeBytes(action)

        return buffer
    }

}
