package rs.emulate.editor.core.workbench.menu

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import rs.emulate.editor.core.action.Action
import javax.inject.Inject

class WorkbenchMenuFactory @Inject constructor(val actions: @JvmSuppressWildcards Set<Action>) {
    fun createMenu(): WorkbenchMenu {
        val menus = mutableMapOf<String, Menu>()

        actions.forEach { action ->
            val actionDef = action.actionDef
            val menuDef = action.menuEntry ?: return@forEach

            val category = menuDef.categories.fold("") { cat, next -> "${cat}.${next}" }
            val categoryTitle = menuDef.categories.last()
            val menu = menus.computeIfAbsent(category) { Menu(categoryTitle) }
            val menuItem = MenuItem(actionDef.id)

            menu.items.add(menuItem)

            menuItem.onAction = EventHandler<ActionEvent> {
                action.handle(it)
            }
        }

        return WorkbenchMenu(menus.values.toList())
    }
}
