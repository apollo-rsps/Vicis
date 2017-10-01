package rs.emulate.legacy.model;

/**
 * Feature flags that control how {@link Model}s are rendered, and what extra attributes they may have.
 */
public enum ModelFeature {
    /**
     * A feature that indicates a model has {@link Face}s that define their own render priority.
     */
    FACE_RENDER_PRIORITY,

    /**
     * A feature that indicates a model is textured.
     */
    FACE_TEXTURES,

    /**
     * A feature that indicates a model has transparent {@link Face}s.
     */
    FACE_TRANSPARENCY,

    /**
     * A feature that indicates a model has {@link Face}s that are mapped to bones in a skeleton.
     */
    FACE_SKINNING,

    /**
     * A feature that indicates a model has {@link Vertex}es that are mapped to bones in a skeleton.
     */
    VERTEX_SKINNING,
}
