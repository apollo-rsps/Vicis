package rs.emulate.legacy.widget.component

data class ContainerItem(val id: Int, val x: Int, val y: Int)

data class ContainerComponent(val scrollLimit: Int, val hidden: Boolean, val items: List<ContainerItem>) : WidgetComponent()