package rs.emulate.editor.core.action

import javafx.event.ActionEvent
import rs.emulate.editor.core.action.annotation.ActionDef
import rs.emulate.editor.core.action.annotation.MenuEntry

interface Action {
    val actionDef: ActionDef
        get() = this.javaClass.getAnnotation(ActionDef::class.java)

    val menuEntry: MenuEntry?
        get() = this.javaClass.getAnnotation(MenuEntry::class.java)

    fun handle(event: ActionEvent)
}
