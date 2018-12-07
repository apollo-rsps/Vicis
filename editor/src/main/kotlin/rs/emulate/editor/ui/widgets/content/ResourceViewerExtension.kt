package rs.emulate.editor.ui.widgets.content

import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceCache

interface ResourceViewerExtension {
    fun createView(resource: Resource, cache: ResourceCache): ResourceViewer?
}
