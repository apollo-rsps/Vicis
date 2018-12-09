package rs.emulate.legacy.widget

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedShort
import java.io.FileNotFoundException
import java.util.ArrayList

/**
 * Decodes data into [Widget]s.
 *
 * @param widgets The [Archive] containing the [Widget]s.
 * @throws FileNotFoundException If the [data][.DATA_FILE_NAME] file could not be found.
 */
class WidgetDecoder(widgets: Archive) {

    /**
     * The Archive containing the Widgets.
     */
    private val buffer = widgets.getEntry(DATA_FILE_NAME).buffer

    /**
     * Decodes the [Widget]s.
     */
    fun decode(): List<Widget> {
        val widgets = ArrayList<Widget>()

        while (buffer.hasRemaining()) {
            widgets.add(decodeWidget())
        }

        return widgets
    }

    /**
     * Decodes a single [Widget].
     */
    private fun decodeWidget(): Widget {
        var id = buffer.getUnsignedShort()
        var parent = -1

        if (id == CHILD_WIDGET_IDENTIFIER) {
            parent = buffer.getUnsignedShort()
            id = buffer.getUnsignedShort()
        }

        val group = WidgetGroup.valueOf(buffer.getUnsignedByte())
        val option = WidgetOption.valueOf(buffer.getUnsignedByte())
        val contentType = buffer.getUnsignedByte()

        val width = buffer.getUnsignedShort()
        val height = buffer.getUnsignedShort()
        val alpha = buffer.getUnsignedByte()

        val hoverId = buffer.getUnsignedByte()
        val hover = when (hoverId) {
            0 -> hoverId - 1 shl java.lang.Byte.SIZE or buffer.getUnsignedByte()
            else -> null
        }

        val scripts = LegacyClientScriptCodec.decode(buffer)

        return TODO()
    }

    companion object {

        /**
         * The identifier that indicates that a Widget has a parent.
         */
        private const val CHILD_WIDGET_IDENTIFIER = 65535

        /**
         * The name of the file containing the Widget data.
         */
        private const val DATA_FILE_NAME = "data"

        /**
         * The amount of sprites used by an [WidgetGroup.INVENTORY] Widget.
         */
        private const val SPRITE_COUNT = 20
    }

}
