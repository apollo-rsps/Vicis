package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.INVENTORY
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.util.Point
import rs.emulate.util.putAsciiString
import rs.emulate.util.putBoolean
import rs.emulate.util.putByte
import java.nio.ByteBuffer

/**
 * Contains properties used by [WidgetGroup.INVENTORY].
 *
 * @param id The id of the TextWidget.
 * @param parent The parent id of the TextWidget.
 * @param optionType The [WidgetOption] of the TextWidget.
 * @param content The content type of the TextWidget.
 * @param width The width of the TextWidget, in pixels.
 * @param height The width of the TextWidget, in pixels.
 * @param alpha The alpha of the TextWidget, in pixels.
 * @param hover The hover id of the TextWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the InventoryWidget.
 * @param hoverText The hover text of the InventoryWidget.
 * @param swappable Whether or not items in the inventory are swappable.
 * @param usable Whether or not the items in the inventory are usable.
 * @param replace Whether or not items in the inventory are replaced (instead of swapped).
 * @param padding The [Point] containing the padding for the sprites.
 * @param sprites The [List] of sprite names.
 * @param spritePoints The List of Points for the sprites.
 * @param actions The List of menu actions.
 */
class InventoryWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hover: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val swappable: Boolean,
    private val usable: Boolean,
    private val replace: Boolean,
    private val padding: Point,
    private val sprites: List<String?>,
    private val spritePoints: List<Point>,
    private val actions: List<String>
) : Widget(id, parent, INVENTORY, optionType, content, width, height, alpha, hover, scripts, option, hoverText) {

    init {
        require(sprites.size == spritePoints.size) { "Sprite names and Points must have an equal size." }
    }

    public override fun encodeBespoke(): ByteBuffer {
        var size = actions.map(String::length).sum() + actions.size
        size += sprites.map { name ->
            (name?.length ?: 0) + 2 * java.lang.Short.BYTES + java.lang.Byte.BYTES
        }.sum()

        val action = ByteBuffer.allocate(size)
        actions.forEach { action.putAsciiString(it) }
        action.flip()

        size = sprites.map { name ->
            (name?.length ?: 0) + 2 * java.lang.Short.BYTES + java.lang.Byte.BYTES
        }.sum()
        val sprite = ByteBuffer.allocate(size)

        for (index in sprites.indices) {
            val name = sprites[index]
            sprite.putBoolean(name != null)

            if (name != null) {
                val point = spritePoints[index]

                sprite.putShort(point.x.toShort()).putShort(point.y.toShort())
                sprite.putAsciiString(name)
            }
        }

        sprite.flip()

        val buffer = ByteBuffer.allocate(6 * java.lang.Byte.BYTES + action.remaining() + sprite.remaining())

        buffer.putBoolean(swappable)
        buffer.putBoolean(actions.isEmpty())
        buffer.putBoolean(usable)
        buffer.putBoolean(replace)

        buffer.putByte(padding.x).putByte(padding.y)
        buffer.put(sprite)
        buffer.put(action)

        return buffer.apply { flip() }
    }

}
