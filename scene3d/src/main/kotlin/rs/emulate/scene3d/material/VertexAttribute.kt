package rs.emulate.scene3d.material

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.Vec3i
import kotlin.reflect.KClass

/**
 * A vertex attribute that acts as the key and data type for vertex data associated with
 * geometry.
 */
sealed class VertexAttribute<T : Any>(val key: String, val type: KClass<T>) {
    object Position : VertexAttribute<Vec3>("position", Vec3::class)
    object Color : VertexAttribute<Vec3>("color", Vec3::class)
    object Normal : VertexAttribute<Vec3>("normal", Vec3::class)
    object TexCoord : VertexAttribute<Vec2>("texCoord", Vec2::class)
    object Index : VertexAttribute<Vec3i>("index", Vec3i::class)
}
