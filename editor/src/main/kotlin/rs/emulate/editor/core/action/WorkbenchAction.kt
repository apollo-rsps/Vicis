package rs.emulate.editor.core.action

import javafx.event.Event
import rs.emulate.editor.core.action.annotation.Action
import rs.emulate.editor.core.action.annotation.MenuEntry
import rs.emulate.editor.utils.javafx.createAsyncEventHandler

interface WorkbenchAction {
    val actionDef: Action
        get() = this.javaClass.getAnnotation(Action::class.java)

    val menuEntry: MenuEntry?
        get() = this.javaClass.getAnnotation(MenuEntry::class.java)

    suspend fun handle(event: Event)

    fun asEventHandler() = createAsyncEventHandler(::handle)
}
