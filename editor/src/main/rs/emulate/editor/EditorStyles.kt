package rs.emulate.editor

import javafx.scene.paint.Color
import tornadofx.*

class EditorStyles : Stylesheet() {
    companion object {
        val wrapper by cssclass()
        val consola by cssclass()
        val propertySheet by cssclass()
        val editorContainer by cssclass("editor-container")
    }

    init {
        root {
            prefHeight = 720.px
            prefWidth = 1024.px
        }

        tabPane {
            hgap = 0.px
            fillWidth = true
            fitToWidth = true
        }
        editorContainer {
            fillWidth = true

            fitToWidth = true
        }

        propertySheet {
            prefWidth = 300.px
            fitToHeight = true
            fitToWidth = true
        }
    }
}