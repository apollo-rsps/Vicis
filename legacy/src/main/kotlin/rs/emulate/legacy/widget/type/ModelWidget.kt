package rs.emulate.legacy.widget.type

import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetGroup
import rs.emulate.legacy.widget.WidgetGroup.MODEL
import rs.emulate.legacy.widget.WidgetOption
import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.legacy.widget.type.ModelWidget.Model
import rs.emulate.shared.util.putBoolean
import rs.emulate.shared.util.putByte
import java.nio.ByteBuffer
import java.util.Arrays

/**
 * A [WidgetGroup.MODEL] [Widget].
 *
 * @param id The id of the ModelWidget.
 * @param parent The parent id of the ModelWidget.
 * @param optionType The [WidgetOption] of the ModelWidget.
 * @param content The content type of the ModelWidget.
 * @param width The width of the ModelWidget, in pixels.
 * @param height The width of the ModelWidget, in pixels.
 * @param alpha The alpha of the ModelWidget, in pixels.
 * @param hover The hover id of the ModelWidget.
 * @param scripts The [List] of [LegacyClientScript]s.
 * @param option The [Option] of the ModelWidget.
 * @param hoverText The hover text of the ModelWidget.
 * @param primary The default [Model].
 * @param secondary The secondary [Model].
 * @param scale The scale of the model.
 * @param pitch The pitch of the model.
 * @param roll The roll of the model.
 */
class ModelWidget(
    id: Int, parent: Int?, optionType: WidgetOption, content: Int, width: Int, height: Int, alpha: Int,
    hover: Int?, scripts: List<LegacyClientScript>, option: Option?, hoverText: String?,
    private val primary: Model,
    private val secondary: Model,
    private val scale: Int,
    private val pitch: Int,
    private val roll: Int
) : Widget(id, parent, MODEL, optionType, content, width, height, alpha, hover, scripts, option, hoverText) {

    /**
     * A model used as part of a [ModelWidget].
     */
    data class Model(val id: Int?, val animation: Int?)

    override fun encodeBespoke(): ByteBuffer {
        var size = 3 * java.lang.Short.BYTES
        for (model in Arrays.asList(primary, secondary)) {
            size += if (model.id != null) java.lang.Short.BYTES else java.lang.Byte.BYTES
            size += if (model.animation != null) java.lang.Short.BYTES else java.lang.Byte.BYTES
        }

        val buffer = ByteBuffer.allocate(size)
        for (value in listOf(primary.id, secondary.id, primary.animation, secondary.animation)) {
            if (value != null) {
                buffer.putByte(value shr 8 + 1)
                buffer.putByte(value)
            } else {
                buffer.putBoolean(false)
            }
        }

        buffer.putShort(scale.toShort())
        buffer.putShort(pitch.toShort())
        buffer.putShort(roll.toShort())

        return buffer.apply { flip() }
    }

}
