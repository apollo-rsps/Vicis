package rs.emulate.editor.workspace.resource

import javafx.collections.ObservableMap
import rs.emulate.legacy.config.SerializableProperty

interface Resource {
    val id: ResourceId
    val properties: ObservableMap<SerializableProperty<*>, *>
}

interface ResourceId {
    val name: String?
}

interface ResourceBundle {

    fun load(id: ResourceId): Resource

    fun list(): Sequence<ResourceId>

}

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle>)
}
