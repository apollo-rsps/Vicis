package rs.emulate.editor.workspace

import io.reactivex.subjects.BehaviorSubject
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndex
import tornadofx.*

class EditorWorkspaceController : Controller() {
    val model : EditorWorkspaceModel by inject()

    /**
     * An event-emitter that emits a list of [ResourceId]'s whenever
     * the list is refreshed.
     */
    val onResourceIndexChanged = BehaviorSubject.create<ResourceIndex>()

    init {
        onResourceIndexChanged.onNext(model.cache.index())
    }
}
