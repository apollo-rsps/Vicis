package rs.emulate.legacy.model

/**
 * A representation of a Model stored in the cache.
 *
 * @param textureCoordinates A pool of texture coordinates, projected into viewspace.
 */
class Model(
    private val features: Set<ModelFeature>,
    val faces: Array<Face>,
    val vertices: Array<Vertex>,
    private val textureCoordinates: Array<TexCoord>
) {

    /**
     * Check if this `Model` has the given feature enabled.
     */
    fun hasFeature(feature: ModelFeature): Boolean {
        return features.contains(feature)
    }

}
