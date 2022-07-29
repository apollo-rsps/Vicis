package rs.emulate.legacy.widget

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.widget.component.*
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec
import rs.emulate.legacy.widget.type.Option
import rs.emulate.util.readAsciiString
import java.io.FileNotFoundException

/**
 * Decodes data into [Widget]s.
 *
 * @param widgets The [Archive] containing the [Widget]s.
 * @throws FileNotFoundException If the [data][.DATA_FILE_NAME] file could not be found.
 */
class WidgetDecoder(private val widgets: Archive) {

    /**
     * The Archive containing the Widgets.
     */
    private val buffer = widgets[DATA_FILE_NAME].buffer

    /**
     * Decodes the [Widget]s.
     */
    fun decode(): List<Widget> {
        val widgetCount = buffer.readUnsignedShort()
        val widgetsList = ArrayList<Widget>(widgetCount)

        while (buffer.readableBytes() > 0) {
            widgetsList.add(decodeWidget())
        }

        return widgetsList
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
        val optionType = WidgetOption.valueOf(buffer.readUnsignedByte().toInt())
        val contentType = buffer.readUnsignedShort()

        val width = buffer.readUnsignedShort()
        val height = buffer.readUnsignedShort()
        val alpha = buffer.readUnsignedByte()

        val hoverId = buffer.readUnsignedByte().toInt()
        val hover = when (hoverId) {
            0 -> null
            else -> hoverId - 1 shl java.lang.Byte.SIZE or buffer.readUnsignedByte().toInt()
        }

        val scripts = LegacyClientScriptCodec.decode(buffer)
        val component = when (group) {
            WidgetGroup.CONTAINER -> {
                val scrollLimit = buffer.readUnsignedShort()
                val hidden = buffer.readUnsignedByte().toInt() == 1
                val numItems = buffer.readUnsignedShort()
                val items = List(numItems) {
                    ContainerItem(buffer.readUnsignedShort(), buffer.readShort().toInt(), buffer.readShort().toInt())
                }

                ContainerComponent(
                    scrollLimit,
                    hidden,
                    items
                )
            }
            WidgetGroup.INVENTORY -> {
                val swappableItems = buffer.readBoolean()
                val hasActions = buffer.readBoolean()
                val usableItems = buffer.readBoolean()
                val replaceItems = buffer.readBoolean()
                val spritePaddingX = buffer.readUnsignedByte().toInt()
                val spritePaddingY = buffer.readUnsignedByte().toInt()

                InventoryComponent(
                    swappableItems,
                    hasActions,
                    usableItems,
                    replaceItems,
                    spritePaddingX,
                    spritePaddingY,
                    List(20) {
                        if (buffer.readBoolean()) {
                            val x = buffer.readShort()
                            val y = buffer.readShort()
                            val name = buffer.readAsciiString()

                            InventorySprite(name, x.toInt(), y.toInt())
                        } else {
                            null
                        }
                    },
                    List(5) { buffer.readAsciiString() }
                )
            }
            WidgetGroup.RECTANGULAR -> {
                val filled = buffer.readBoolean()
                val defaultColour = buffer.readUnsignedInt()
                val alternateColour = buffer.readUnsignedInt()
                val defaultHoverColour = buffer.readUnsignedInt()
                val alternateHoverColour = buffer.readUnsignedInt()

                RectangleComponent(
                    filled,
                    defaultColour,
                    alternateColour,
                    defaultHoverColour,
                    alternateHoverColour,
                )
            }
            WidgetGroup.MODEL -> {
                var media = buffer.readUnsignedByte().toInt()
                var defaultMediaType = -1
                var defaultMedia = 0

                if (media != 0) {
                    defaultMediaType = 1
                    defaultMedia = media - 1 shl 8 or buffer.readUnsignedByte().toInt()
                }

                media = buffer.readUnsignedByte().toInt()
                var alternateMediaType = -1
                var alternateMedia = 0
                if (media != 0) {
                    alternateMediaType = 1
                    alternateMedia = media - 1 shl 8 or buffer.readUnsignedByte().toInt()
                }

                media = buffer.readUnsignedByte().toInt()
                val defaultSequenceId = if (media != 0) media - 1 shl 8 or buffer.readUnsignedByte().toInt() else -1

                media = buffer.readUnsignedByte().toInt()
                val alternateSequenceId = if (media != 0) media - 1 shl 8 or buffer.readUnsignedByte().toInt() else -1

                val mediaScale = buffer.readUnsignedShort()
                val mediaRoll = buffer.readUnsignedShort()
                val mediaYaw = buffer.readUnsignedShort()

                ModelComponent(
                    defaultMediaType,
                    defaultMedia,
                    alternateMediaType,
                    alternateMedia,
                    defaultSequenceId,
                    alternateSequenceId,
                    mediaScale,
                    mediaRoll,
                    mediaYaw
                )
            }
            WidgetGroup.SPRITE -> {
                val defaultName = buffer.readAsciiString()
                val alternateName = buffer.readAsciiString()

                SpriteComponent(defaultName, alternateName)
            }
            WidgetGroup.TEXT -> {
                val centered = buffer.readBoolean()
                val font = buffer.readByte().toInt()
                val shadowed = buffer.readBoolean()
                val defaultText = buffer.readAsciiString()
                val altText = buffer.readAsciiString()
                val defaultColour = buffer.readUnsignedInt()
                val alternateColour = buffer.readUnsignedInt()
                val defaultHoverColour = buffer.readUnsignedInt()
                val alternateHoverColour = buffer.readUnsignedInt()

                TextComponent(
                    centered,
                    font,
                    shadowed,
                    defaultText,
                    altText,
                    defaultColour,
                    alternateColour,
                    defaultHoverColour,
                    alternateHoverColour
                )
            }
            WidgetGroup.ITEM_LIST -> {
                val centered = buffer.readBoolean()
                val font = buffer.readByte().toInt()
                val shadowed = buffer.readBoolean()
                val paddingX = buffer.readShort()
                val paddingY = buffer.readShort()
                val hasActions = buffer.readBoolean()
                val actions = List(5) { buffer.readAsciiString() }

                ItemListComponent(centered, font, shadowed, paddingX, paddingY, hasActions, actions)
            }
            WidgetGroup.UNKNOWN_TEXT -> {
                UnknownTextComponent(buffer.readAsciiString())
            }
            WidgetGroup.MODEL_LIST -> TODO()
        }

        val option = if (optionType == WidgetOption.USABLE || group == WidgetGroup.INVENTORY) {
            Option(buffer.readAsciiString(), buffer.readAsciiString(), buffer.readUnsignedShort())
        } else {
            null
        }

        var hoverText: String? = null
        if (optionType == WidgetOption.OK || optionType == WidgetOption.TOGGLE_SETTING || optionType == WidgetOption.RESET_SETTING || optionType == WidgetOption.CONTINUE) {
            hoverText = buffer.readAsciiString()
        }

        return Widget(
            id,
            parent,
            group,
            optionType,
            contentType.toInt(),
            width,
            height,
            alpha.toInt(),
            hoverId,
            scripts,
            component,
            option,
            hoverText
        )
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
