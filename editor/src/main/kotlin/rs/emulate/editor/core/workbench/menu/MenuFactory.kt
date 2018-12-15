package rs.emulate.editor.core.workbench.menu

import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import rs.emulate.editor.core.action.WorkbenchAction
import rs.emulate.editor.utils.javafx.createAsyncEventHandler
import javax.inject.Inject

class MenuFactory @Inject constructor(val actions: @JvmSuppressWildcards Set<WorkbenchAction>) {
    fun createMenu(): WorkbenchMenu {
        val menus = mutableMapOf<String, Menu>()
        val menu = Menu("File")
        menus["File"] = menu

        actions.forEach { action ->
            val actionDef = action.actionDef
            val menuItem = MenuItem(actionDef.id)

            menu.items.add(menuItem)
            menuItem.onAction = createAsyncEventHandler(action::handle)
        }

        return WorkbenchMenu(menus.values.toList())
    }
}
