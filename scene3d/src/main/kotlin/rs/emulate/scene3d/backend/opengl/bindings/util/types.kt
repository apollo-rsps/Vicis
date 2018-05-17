import glm_.mat2x2.Mat2
import glm_.mat3x3.Mat3
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import org.lwjgl.opengl.GL20
import kotlin.reflect.KClass

/**
 * Get the GLM type representing the [glType]
 */
fun glmType(glType: Int): KClass<out Any> = when (glType) {
    GL20.GL_FLOAT_VEC2 -> Vec2::class
    GL20.GL_FLOAT_VEC3 -> Vec3::class
    GL20.GL_FLOAT_VEC4 -> Vec4::class
    GL20.GL_FLOAT_MAT2 -> Mat2::class
    GL20.GL_FLOAT_MAT3 -> Mat3::class
    GL20.GL_FLOAT_MAT4 -> Mat4::class
    else -> throw IllegalArgumentException("Unknown type {$glType}")
}
