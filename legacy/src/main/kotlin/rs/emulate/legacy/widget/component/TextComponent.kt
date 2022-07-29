package rs.emulate.legacy.widget.component

data class TextComponent(
    val centered: Boolean,
    val font: Int,
    val shadowed: Boolean,
    val defaultText: String,
    val textUnk: String,
    val defaultColour: Long,
    val alternateColour: Long,
    val defaultHoverColour: Long,
    val alternateHoverColour: Long
) : WidgetComponent()