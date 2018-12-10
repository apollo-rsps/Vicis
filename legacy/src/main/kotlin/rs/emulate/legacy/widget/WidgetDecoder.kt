package rs.emulate.legacy.widget

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec
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
    private val buffer = widgets[DATA_FILE_NAME].buffer

    /**
     * Decodes the [Widget]s.
     */
    fun decode(): List<Widget> {
        val widgets = ArrayList<Widget>()

        while (buffer.readableBytes() > 0) {
            widgets.add(decodeWidget())
        }

        return widgets
    }

    /**
     * Decodes a single [Widget].
     */
    private fun decodeWidget(): Widget {
        var id = buffer.readUnsignedShort()
        var parent = -1

        if (id == CHILD_WIDGET_IDENTIFIER) {
            parent = buffer.readUnsignedShort()
            id = buffer.readUnsignedShort()
        }

        val group = WidgetGroup.valueOf(buffer.readUnsignedByte().toInt())
        val option = WidgetOption.valueOf(buffer.readUnsignedByte().toInt())
        val contentType = buffer.readUnsignedByte()

        val width = buffer.readUnsignedShort()
        val height = buffer.readUnsignedShort()
        val alpha = buffer.readUnsignedByte()

        val hoverId = buffer.readUnsignedByte().toInt()
        val hover = when (hoverId) {
            0 -> hoverId - 1 shl java.lang.Byte.SIZE or buffer.readUnsignedByte().toInt()
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
