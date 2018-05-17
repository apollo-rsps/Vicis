package rs.emulate.editor

import rs.emulate.editor.ui.startup.EditorStartupView
import tornadofx.*

class EditorApplication : App(EditorStartupView::class) {
    init {
        importStylesheet("/styles.css")
        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<EditorApplication>()
}
