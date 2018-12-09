package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.TEXT
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.util.putAsciiString
import rs.emulate.util.putBoolean
import rs.emulate.util.putByte
import java.nio.ByteBuffer

/**
 * A [WidgetGroup.TEXT] [Widget].
 *
 * @param id The id of the TextWidget.
 * @param parent The parent id of the TextWidget.
 * @param optionType The [WidgetOption] of the TextWidget.
 * @param content The content type of the TextWidget.
 * @param width The width of the TextWidget, in pixels.
 * @param height The width of the TextWidget, in pixels.
 * @param alpha The alpha of the TextWidget, in pixels.
 * @param hoverId The hover id of the TextWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the TextWidget.
 * @param hoverText The hover text of the TextWidget.
 * @param centered Whether or not the text is centered.
 * @param colour The [ColourPair] containing the default and secondary colours of the text.
 * @param defaultText The default text displayed.
 * @param font The id of the font used to draw the text with.
 * @param hover The [ColourPair] containing the default and secondary hover colours.
 * @param secondaryText The secondary text, displayed when a ClientScript state has changed.
 * @param shadowed Whether or not text is drawn with shadowing.
 */
class TextWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hoverId: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val centered: Boolean,
    private val colour: ColourPair,
    private val defaultText: String,
    private val font: Int,
    private val hover: ColourPair,
    private val secondaryText: String,
    private val shadowed: Boolean
) : Widget(id, parent, TEXT, optionType, content, width, height, alpha, hoverId, scripts, option, hoverText) {

    override fun encodeBespoke(): ByteBuffer {
        val primary = ByteBuffer.allocate(defaultText.length + java.lang.Byte.BYTES)
        primary.putAsciiString(defaultText).flip()

        val secondary = ByteBuffer.allocate(secondaryText.length + java.lang.Byte.BYTES)
        secondary.putAsciiString(secondaryText).flip()

        val buffer = ByteBuffer.allocate(
            3 * java.lang.Byte.BYTES + 4 * Integer.BYTES + primary.remaining() + secondary.remaining()
        )

        buffer.putBoolean(centered)
        buffer.putByte(font)
        buffer.putBoolean(shadowed)

        buffer.put(primary).put(secondary)

        for (pair in listOf(colour, hover)) {
            buffer.putInt(pair.default)
            buffer.putInt(pair.secondary)
        }

        return buffer.apply { flip() }
    }

}
