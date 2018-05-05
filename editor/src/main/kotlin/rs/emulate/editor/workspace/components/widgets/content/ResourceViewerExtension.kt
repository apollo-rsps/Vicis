package rs.emulate.editor.workspace.components.widgets.content

import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache

interface ResourceViewerExtension {
    fun createView(resource: Resource, cache: ResourceCache): ResourceViewer
}
