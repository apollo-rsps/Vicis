package rs.emulate.legacy.widget.component

data class ModelComponent(
    val defaultMediaType: Int,
    val defaultMedia: Int,
    val alternateMediaType: Int,
    val alternateMedia: Int,
    val defaultSequenceId: Int,
    val alternateSequenceId: Int,
    val scale: Int,
    val roll: Int,
    val yaw: Int
) : WidgetComponent() {
}