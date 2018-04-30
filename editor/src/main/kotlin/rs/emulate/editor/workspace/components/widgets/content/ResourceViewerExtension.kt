package rs.emulate.editor.workspace.components.widgets.content

import rs.emulate.editor.workspace.resource.Resource

interface ResourceViewerExtension {
    fun createView(resource: Resource): ResourceViewer
}
