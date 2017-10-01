package rs.emulate.editor.event

import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceIdentifier
import tornadofx.*

data class ResourceClosedEvent(val identifier: ResourceIdentifier) : FXEvent(EventBus.RunOn.ApplicationThread)

data class ResourceLoadedEvent(val resource: Resource) : FXEvent(EventBus.RunOn.ApplicationThread)
