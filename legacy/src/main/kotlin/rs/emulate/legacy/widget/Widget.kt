package rs.emulate.legacy.widget

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec
import rs.emulate.legacy.widget.type.Option
import rs.emulate.util.writeAsciiString
import java.lang.Byte
import java.lang.Short

/**
 * A user interface component.
 *
 * @param id The id of the Widget.
 * @param parentId The parent id of the Widget.
 * @param group The [WidgetGroup] of the Widget.
 * @param optionType The [WidgetOption] of the Widget.
 * @param contentType The content type of the Widget.
 * @param width The width of the Widget, in pixels.
 * @param height The width of the Widget, in pixels.
 * @param alpha The alpha of the Widget, in pixels.
 * @param hoverId The hover id of the Widget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the Widget.
 * @param hoverText The hover text of the Widget.
 */
abstract class Widget(
    val id: Int,
    val parentId: Int?,
    val group: WidgetGroup,
    val optionType: WidgetOption,
    val contentType: Int,
    val width: Int,
    val height: Int,
    val alpha: Int,
    val hoverId: Int?,
    val scripts: List<LegacyClientScript>,
    private val option: Option?,
    private val hoverText: String?
) {

    /**
     * Encodes this Widget into a [ByteBuf].
     */
    fun encode(): ByteBuf {
        val bespoke = encodeBespoke()
        val scripts = LegacyClientScriptCodec.encode(scripts)
        val option = encodeOption()
        val hoverText = encodeHoverText()

        val parentSize = if (parentId != null) 2 * java.lang.Short.BYTES else java.lang.Short.BYTES
        val hoverSize = if (hoverId != null) java.lang.Short.BYTES else java.lang.Byte.BYTES
        val metaSize = 4 * java.lang.Short.BYTES + 3 * java.lang.Byte.BYTES
        val specific =
            scripts.readableBytes() + bespoke.readableBytes() + option.readableBytes() + hoverText.readableBytes()

        val size = parentSize + metaSize + hoverSize + specific
        val buffer = Unpooled.buffer(size) // TODO use composite buffer

        if (parentId != null) {
            buffer.writeShort(PARENT_ID_PRESENT)
            buffer.writeShort(id)
            buffer.writeShort(parentId)
        } else {
            buffer.writeShort(id)
        }

        buffer.writeByte(group.value)
        buffer.writeByte(optionType.value)

        buffer.writeShort(contentType)
        buffer.writeShort(width)
        buffer.writeShort(height)

        buffer.writeByte(alpha)

        if (hoverId != null) {
            val value = hoverId
            buffer.writeByte(value + 1 shr 8)
            buffer.writeByte(value)
        } else {
            buffer.writeByte(HOVER_ID_ABSENT)
        }

        buffer.writeBytes(scripts)
        buffer.writeBytes(bespoke)
        buffer.writeBytes(option)

        return buffer
    }

    /**
     * Encodes the bespoke data belonging to this Widget into a [ByteBuf].
     */
    protected abstract fun encodeBespoke(): ByteBuf

    /**
     * Encodes the hover text of this Widget into a [ByteBuf].
     */
    private fun encodeHoverText(): ByteBuf {
        if (hoverText != null) {
            val text = hoverText

            return Unpooled.buffer(text.length + java.lang.Byte.BYTES)
                .apply { writeAsciiString(text) }
        }

        return Unpooled.EMPTY_BUFFER
    }

    /**
     * Encodes the [Option] of this Widget into a [ByteBuf].
     */
    private fun encodeOption(): ByteBuf {
        if (option != null) {
            require(group == WidgetGroup.INVENTORY || optionType == WidgetOption.USABLE) {
                "Only usable or inventory widgets may have an option."
            }

            val option = option
            val circumfix = option.circumfix
            val text = option.text

            return Unpooled.buffer(circumfix.length + text.length + 2 * Byte.BYTES + Short.BYTES).apply {
                writeAsciiString(circumfix)
                writeAsciiString(text)
                writeShort(option.attributes)
            }
        }

        return Unpooled.EMPTY_BUFFER
    }

    companion object {

        /**
         * The value indicating that a Widget does not have a hover id.
         */
        private const val HOVER_ID_ABSENT = 0

        /**
         * The value indicating that a Widget has a parent id.
         */
        private const val PARENT_ID_PRESENT = 65535
    }

}
