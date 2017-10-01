package rs.emulate.legacy.model

/**
 * Feature flags that control how [Model]s are rendered, and what extra attributes they may have.
 */
enum class ModelFeature {

    /**
     * Indicates that a model has [Face]s that define their own render priority.
     */
    FACE_RENDER_PRIORITY,

    /**
     * Indicates that a model is textured.
     */
    FACE_TEXTURES,

    /**
     * Indicates that a model has transparent [Face]s.
     */
    FACE_TRANSPARENCY,

    /**
     * Indicates that a model has [Face]s that are mapped to bones in a skeleton.
     */
    FACE_SKINNING,

    /**
     * Indicates that a model has [Vertex]es that are mapped to bones in a skeleton.
     */
    VERTEX_SKINNING

}
