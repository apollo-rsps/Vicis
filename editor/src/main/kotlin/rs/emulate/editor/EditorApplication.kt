package rs.emulate.editor

import rs.emulate.editor.startup.EditorStartupView
import rs.emulate.editor.workspace.ObjectResourceBundle
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.App
import java.nio.file.Paths

class EditorApplication : App(EditorStartupView::class)

fun main(args: Array<String>) {
    val fs = IndexedFileSystem(Paths.get("./data/resources/377"), AccessMode.READ)
    ObjectResourceBundle(fs)

//    launch<EditorApplication>()
}
