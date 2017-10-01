package rs.emulate.legacy.model

/**
 * A model face.
 *
 * @param a The index of the [Vertex] that represents the bottom left of this [Face].
 * @param b The index of the [Vertex] that represents the bottom right of this [Face].
 * @param c The index of the [Vertex] that represents the bottom top of this [Face].
 * @param renderPriority The render priority of this [Face] (`-1` to use the model's overall render priority).
 * @param alpha The alpha of this [Face] (`-1` being none).
 * @param bone The id of the bone this [Face] is attached to (`-1` being none).
 */
data class Face(
    val a: Int,
    val b: Int,
    val c: Int,
    val colour: Int,
    val renderPriority: Int,
    val alpha: Int,
    val bone: Int,
    val texturePointer: Int
)
