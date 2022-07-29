package rs.emulate.legacy.widget.component

data class InventorySprite(val name: String, val x: Int, val y: Int)

data class InventoryComponent(
    val swappableItems: Boolean,
    val hasActions: Boolean,
    val usableItems: Boolean,
    val replaceItems: Boolean,
    val spritePaddingX: Int,
    val spritePaddingY: Int,
    val sprites: List<InventorySprite?>,
    val actions: List<String>,
) : WidgetComponent()