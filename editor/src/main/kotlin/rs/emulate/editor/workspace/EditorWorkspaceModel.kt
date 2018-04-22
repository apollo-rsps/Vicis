package rs.emulate.editor.workspace

import io.reactivex.subjects.BehaviorSubject
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.index.ResourceIndex
import tornadofx.ViewModel

class EditorWorkspaceModel(val cache: ResourceCache = ResourceCache()) : ViewModel() {
    val onResourceSelected = BehaviorSubject.create<Resource>()
    val onResourceSelectionError = BehaviorSubject.create<Throwable>()
    val onResourceIndexUpdate = BehaviorSubject.create<ResourceIndex>()
}
