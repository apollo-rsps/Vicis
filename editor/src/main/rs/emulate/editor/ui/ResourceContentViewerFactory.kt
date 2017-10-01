package rs.emulate.editor.ui

import org.springframework.stereotype.Component
import rs.emulate.editor.resource.NullContentModel
import rs.emulate.editor.resource.ResourceContentModel
import kotlin.reflect.KClass

interface ResourceContentViewerFactory<out T : ResourceContentModel, out V : ResourceContentModelViewer<T>> {
    fun supports(kClass: KClass<out ResourceContentModel>): Boolean
    fun create(contentModel: ResourceContentModel): V
}

@Component
class NullContentViewerFactory : ResourceContentViewerFactory<NullContentModel, NullContentModelViewer> {
    override fun supports(kClass: KClass<out ResourceContentModel>): Boolean {
        return kClass == NullContentModel::class
    }

    override fun create(contentModel: ResourceContentModel): NullContentModelViewer {
        return NullContentModelViewer(contentModel as NullContentModel)
    }
}