package rs.emulate.editor.legacy.config.npc

import org.joml.Vector3f
import rs.emulate.editor.core.content.capabilities.ResourceViewerSupport
import rs.emulate.editor.core.workbench.viewer.ResourceView
import rs.emulate.editor.core.workbench.viewer.component.SceneComponent
import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.LegacyFileLoader
import rs.emulate.editor.vfs.VirtualFileLoader
import rs.emulate.legacy.config.npc.NpcDefinitionDecoder
import rs.emulate.legacy.model.Face
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.ModelDecoder
import rs.emulate.legacy.model.Vertex
import rs.emulate.scene3d.Mesh
import rs.emulate.util.compression.gunzip
import java.awt.Color

class NpcViewerSupport : ResourceViewerSupport<LegacyFileId.ConfigEntry> {

    override fun createViewer(
        id: LegacyFileId.ConfigEntry,
        loader: VirtualFileLoader<LegacyFileId.ConfigEntry>
    ): ResourceView {
        loader as LegacyFileLoader // TODO yuck

        val npcdef = loader.load(id)!!
        val definition = NpcDefinitionDecoder.decode(id.file, npcdef)
        val models = definition.models.map {
            val buffer = loader.load(LegacyFileId.FileEntry(1, it))!!
            ModelDecoder(buffer.gunzip()).decode()
        }

        val mesh = meshFromModels(models)

        return object : ResourceView() {
            override val root = SceneComponent()

            init {
                root.addChild(mesh)
            }
        }
    }

    // TODO temporary
    private fun Vertex.toVec3f() = Vector3f(x * 0.01f, -y * 0.01f, z * 0.01f)

    private fun meshFromModels(models: List<Model>): Mesh {
        val positions = mutableListOf<Vector3f>()
        val colors = mutableListOf<Vector3f>()

        models.forEach { (_, faces, vertices) ->
            val sorted = faces.sortedByDescending(Face::renderPriority)

            positions += sorted.flatMap {
                listOf(
                    vertices[it.a].toVec3f(),
                    vertices[it.b].toVec3f(),
                    vertices[it.c].toVec3f()
                )
            }

            colors += sorted.flatMap { listOf(it.colour, it.colour, it.colour) }
                .map { colour ->
                    val h = (colour shr 10)
                    val s = (colour shr 7) and 7
                    val l = colour and 0x7F

                    val color = Color.getHSBColor(h / 60.0f, s / 8.0f, l / 128.0f)
                    Vector3f(color.red / 256.0f, color.green / 256.0f, color.blue / 256.0f)
                }
        }

        return Mesh().also {
            it.positions = positions
            it.colors = colors
        }
    }

}
