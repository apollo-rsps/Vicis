package rs.emulate.legacy.widget.component

data class ItemListComponent(
    val centered: Boolean,
    val font: Int,
    val shadowed: Boolean,
    val paddingX: Short,
    val paddingY: Short,
    val hasActions: Boolean,
    val actions: List<String>
) : WidgetComponent() {

}
