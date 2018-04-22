package rs.emulate.editor.workspace

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndex
import tornadofx.*

class EditorWorkspaceModel(val cache: ResourceCache = ResourceCache()) : ViewModel() {
    val onResourceSelected = BehaviorSubject.create<Resource>()
    val onResourceSelectionError = BehaviorSubject.create<Throwable>()
    val onResourceIndexUpdate = BehaviorSubject.create<ResourceIndex>()
}
