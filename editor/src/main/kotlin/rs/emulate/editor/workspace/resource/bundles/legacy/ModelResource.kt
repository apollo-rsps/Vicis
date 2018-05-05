package rs.emulate.editor.workspace.resource.bundles.legacy

import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.shared.util.CompressionUtils

data class ModelResource(override val id: ResourceId, val model: Model) : Resource {

    fun recoloured(changes: Map<Int, Int>): ModelResource {
        return copy(model = model.recolour(changes))
    }

}

data class ModelResourceId(val id: Int) : ResourceId

class ModelResourceBundle(private val fs: IndexedFileSystem) : ResourceBundle<ModelResourceId> {
    override val idType = ModelResourceId::class

    override fun load(id: ModelResourceId): Resource {
        val compressed = fs.getFile(MODEL_INDEX, id.id)
        val decompressed = CompressionUtils.gunzip(compressed)

        val decoder = ModelDecoder(decompressed)
        return ModelResource(id, decoder.decode())
    }

    override fun index(index: ResourceIndexBuilder) {
        val count = fs.getFileCount(MODEL_INDEX)

        (0 until count).forEach { file ->
            index.category("Models") {
                item {
                    id = ModelResourceId(file)
                    label = file.toString()
                }
            }
        }
    }

    private companion object {
        const val MODEL_INDEX = 1
    }

}
