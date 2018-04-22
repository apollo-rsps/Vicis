package rs.emulate.editor.workspace.components.menu

import tornadofx.*

class EditorMenu : Fragment() {
    override val root = menubar {
        menu(messages["file"]) {
            item(messages["file.save"])
            item(messages["file.close"])
        }

        menu(messages["help"]) {
            item(messages["help.about"])
        }
    }
}
