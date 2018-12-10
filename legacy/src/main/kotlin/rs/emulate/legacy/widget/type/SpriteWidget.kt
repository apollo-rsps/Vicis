package rs.emulate.legacy.widget.type

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.SPRITE
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.util.writeAsciiString

/**
 * A [WidgetGroup.SPRITE] [Widget].
 *
 * @param id The id of the SpriteWidget.
 * @param parent The parent id of the SpriteWidget.
 * @param optionType The [WidgetOption] of the SpriteWidget.
 * @param content The content type of the SpriteWidget.
 * @param width The width of the SpriteWidget, in pixels.
 * @param height The width of the SpriteWidget, in pixels.
 * @param alpha The alpha of the SpriteWidget, in pixels.
 * @param hover The hover id of the SpriteWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the SpriteWidget.
 * @param hoverText The hover text of the SpriteWidget.
 * @param primary The name of the default Sprite.
 * @param secondary The name of the secondary Sprite.
 */
class SpriteWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hover: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val primary: String,
    private val secondary: String
) : Widget(id, parent, SPRITE, optionType, content, width, height, alpha, hover, scripts, option, hoverText) {

    override fun encodeBespoke(): ByteBuf {
        val buffer = Unpooled.buffer(primary.length + secondary.length + 2 * java.lang.Byte.BYTES)
        buffer.writeAsciiString(primary)
        buffer.writeAsciiString(secondary)

        return buffer
    }

}
