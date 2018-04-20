package rs.emulate.legacy.widget

import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec
import rs.emulate.legacy.widget.type.Option
import rs.emulate.shared.util.DataBuffer

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
     * Encodes this Widget into a [DataBuffer].
     */
    fun encode(): DataBuffer {
        val bespoke = encodeBespoke()
        val scripts = LegacyClientScriptCodec.encode(scripts)
        val option = encodeOption()
        val hoverText = encodeHoverText()

        val parentSize = if (parentId != null) 2 * java.lang.Short.BYTES else java.lang.Short.BYTES
        val hoverSize = if (hoverId != null) java.lang.Short.BYTES else java.lang.Byte.BYTES
        val metaSize = 4 * java.lang.Short.BYTES + 3 * java.lang.Byte.BYTES
        val specific = scripts.remaining() + bespoke.remaining() + option.remaining() + hoverText.remaining()

        val size = parentSize + metaSize + hoverSize + specific

        val buffer = DataBuffer.allocate(size)

        if (parentId != null) {
            buffer.putShort(PARENT_ID_PRESENT)
            buffer.putShort(id)
            buffer.putShort(parentId)
        } else {
            buffer.putShort(id)
        }

        buffer.putByte(group.value)
        buffer.putByte(optionType.value)

        buffer.putShort(contentType)
        buffer.putShort(width)
        buffer.putShort(height)

        buffer.putByte(alpha)

        if (hoverId != null) {
            val value = hoverId
            buffer.putByte(value + 1 shr 8)
            buffer.putByte(value)
        } else {
            buffer.putByte(HOVER_ID_ABSENT)
        }

        buffer.put(scripts)
        buffer.put(bespoke)
        buffer.put(option)

        return buffer.flip().asReadOnlyBuffer()
    }

    /**
     * Encodes the bespoke data belonging to this Widget into a [DataBuffer].
     */
    protected abstract fun encodeBespoke(): DataBuffer

    /**
     * Encodes the hover text of this Widget into a [DataBuffer].
     */
    private fun encodeHoverText(): DataBuffer {
        if (hoverText != null) {
            val text = hoverText

            return DataBuffer.allocate(text.length + java.lang.Byte.BYTES).apply { putAsciiString(text) }
                .flip()
                .asReadOnlyBuffer()
        }

        return DataBuffer.allocate(0)
    }

    /**
     * Encodes the [Option] of this Widget into a [DataBuffer].
     */
    private fun encodeOption(): DataBuffer {
        if (option != null) {
            require(group == WidgetGroup.INVENTORY || optionType == WidgetOption.USABLE) {
                "Only usable or inventory widgets may have an option."
            }

            val option = option
            val circumfix = option.circumfix
            val text = option.text

            val buffer = DataBuffer.allocate(
                circumfix.length + text.length + 2 * java.lang.Byte.BYTES + java.lang.Short.BYTES
            )
            buffer.putAsciiString(circumfix).putAsciiString(text)

            val attributes = option.attributes
            buffer.putShort(attributes)

            return buffer.flip().asReadOnlyBuffer()
        }

        return DataBuffer.allocate(0)
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
