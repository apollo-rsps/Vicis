package rs.emulate.editor.utils.javafx

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseEvent

fun Node.onDoubleClick(handler: EventHandler<MouseEvent>) {
    this.setOnMouseClicked {  }
}
