package rs.emulate.editor

import rs.emulate.editor.startup.EditorStartupView
import tornadofx.*

class EditorApplication : App(EditorStartupView::class)

fun main(args: Array<String>) = launch<EditorApplication>()
