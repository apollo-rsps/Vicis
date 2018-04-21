package rs.emulate.editor.workspace

import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.`object`.DefaultObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectProperty

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
