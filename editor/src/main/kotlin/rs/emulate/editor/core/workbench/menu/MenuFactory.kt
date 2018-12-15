package rs.emulate.editor.core.workbench.menu

import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import rs.emulate.editor.core.action.WorkbenchAction
import rs.emulate.editor.utils.javafx.createAsyncEventHandler
import javax.inject.Inject

class MenuFactory @Inject constructor(val actions: @JvmSuppressWildcards Set<WorkbenchAction>) {
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
            menuItem.onAction = createAsyncEventHandler(action::handle)
        }

        return WorkbenchMenu(menus.values.toList())
    }
}
