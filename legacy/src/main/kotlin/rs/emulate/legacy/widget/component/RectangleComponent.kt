package rs.emulate.legacy.widget.component

data class RectangleComponent(
    val filled: Boolean,
    val defaultColour: Long,
    override val alternateColour: Long,
    override val defaultHoverColour: Long,
    override val alternateHoverColour: Long
) : WidgetComponent(), WithForegroundColours