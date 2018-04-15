package rs.emulate.editor.resource

import com.jme3.math.Vector3f
import rs.emulate.editor.ui.viewers.MeshContentModel
import rs.emulate.legacy.model.Model

class ModelResource(displayName: String, identifier: ResourceIdentifier, val model: Model) : Resource(
    displayName,
    identifier
) {
    override fun createContentModel(store: ResourceStore): ResourceContentModel {
        val vertices = model.vertices.map { Vector3f(it.x * 1f, it.y * 1f, it.z * 1f) }
        val faces = model.faces.flatMap { listOf(it.a, it.b, it.c) }

        return MeshContentModel(vertices.toTypedArray(), faces.toIntArray())
    }
}

fun Collection<ModelResource>.compoundContentModel(): ResourceContentModel {
    val vertices = mutableListOf<Vector3f>()
    val faces = mutableListOf<Int>()

    map(ModelResource::model).forEach { model ->
        vertices += model.vertices.map { Vector3f(it.x * 1f, it.y * 1f, it.z * 1f) }
        faces += model.faces.flatMap { listOf(it.a, it.b, it.c) }
    }

    return MeshContentModel(vertices.toTypedArray(), faces.toIntArray())
}
