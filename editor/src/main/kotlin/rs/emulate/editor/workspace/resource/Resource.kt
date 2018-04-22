package rs.emulate.editor.workspace.resource

import javafx.collections.ObservableMap
import rs.emulate.legacy.config.SerializableProperty

interface Resource {
    val id: ResourceId
    val properties: ObservableMap<SerializableProperty<*>, *>
}
