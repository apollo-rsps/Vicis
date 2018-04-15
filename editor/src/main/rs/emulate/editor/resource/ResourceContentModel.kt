package rs.emulate.editor.resource

import javafx.beans.value.ObservableValue

interface ResourceContentModel {
    fun properties(): List<ObservableValue<*>>
}

class NullContentModel : ResourceContentModel {
    override fun properties(): List<ObservableValue<*>> {
        return emptyList()
    }
}