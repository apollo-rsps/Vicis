package rs.emulate.editor.workspace.resource

import javafx.collections.ObservableMap
import rs.emulate.legacy.config.SerializableProperty

interface ResourceId {
    val name: String?
}

interface Resource<PropType : Any> {
    val id: ResourceId
    val properties: ObservableMap<SerializableProperty<PropType>, *>
}

interface ResourceBundle {

    fun load(id: ResourceId): Resource<*>

    fun list(): Sequence<ResourceId>

}

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle>)
}
