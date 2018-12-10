package rs.emulate.scene3d.backend.opengl.bindings.util

import org.joml.*
import org.lwjgl.opengl.GL20
import kotlin.reflect.KClass

/**
 * Get the GLM type representing the [glType]
 */
fun glmType(glType: Int): KClass<out Any> = when (glType) {
    GL20.GL_FLOAT_VEC2-> Vector2f::class
    GL20.GL_FLOAT_VEC3 -> Vector3f::class
    GL20.GL_FLOAT_VEC4 -> Vector4f::class
    GL20.GL_FLOAT_MAT2 -> Vector4f::class
    GL20.GL_FLOAT_MAT3 -> Matrix3f::class
    GL20.GL_FLOAT_MAT4 -> Matrix4f::class
    else -> throw IllegalArgumentException("Unknown type {$glType}")
}
