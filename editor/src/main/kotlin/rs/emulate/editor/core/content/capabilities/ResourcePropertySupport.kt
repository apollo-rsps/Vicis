package rs.emulate.editor.core.content.capabilities

import org.controlsfx.control.PropertySheet

interface ResourcePropertySupport {
    fun createProperties() : List<PropertySheet.Item>
}
