package rs.emulate.scene3d.material

import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.reflect.KClass

/**
 * A vertex attribute that acts as the key and data type for vertex data associated with
 * geometry.
 */
sealed class VertexAttribute<T : Any>(val key: String, val type: KClass<T>) {
    object Position : VertexAttribute<Vector3f>("position", Vector3f::class)
    object Color : VertexAttribute<Vector3f>("color", Vector3f::class)
    object Normal : VertexAttribute<Vector3f>("normal", Vector3f::class)
    object TexCoord : VertexAttribute<Vector2f>("texCoord", Vector2f::class)
    object Index : VertexAttribute<Vector3i>("index", Vector3i::class)
}
