package rs.emulate.editor.workspace.components.scene3d

import glm_.vec3.Vec3
import rs.emulate.legacy.model.Model
import rs.emulate.legacy.model.Vertex
import rs.emulate.scene3d.Mesh
import java.awt.Color

fun Vertex.toVec3f() = Vec3(x * 0.01f, -y * 0.01f, z * 0.01f)
fun meshFromModels(vararg models: Model): Mesh {
    val positions = mutableListOf<Vec3>()
    val colors = mutableListOf<Vec3>()

    models.forEach { model ->
        positions += model.faces.flatMap {
            listOf(
                model.vertices[it.a].toVec3f(),
                model.vertices[it.b].toVec3f(),
                model.vertices[it.c].toVec3f()
            )
        }

        colors += model.faces
            .flatMap { listOf(it.colour, it.colour, it.colour) }
            .map {
                val colour = it
                val h = (colour shr 10)
                val s = (colour shr 7) and 7
                val l = colour and 0x7F

                val color = Color.getHSBColor(h / 60.0f, s / 8.0f, l / 128.0f)

                Vec3(color.red / 256.0f, color.green / 256.0f, color.blue / 256.0f)
            }
    }

    val mesh = Mesh()
    mesh.positions = positions
    mesh.colors = colors

    return mesh
}

