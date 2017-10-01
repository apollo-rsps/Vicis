package rs.emulate.legacy.model;

import java.util.Set;

/**
 * A representation of a Model stored in the cache.
 */
public final class Model {

    /**
     * A set of {@link ModelFeature}s used by this {@code Model}.
     */
    private final Set<ModelFeature> features;

    /**
     * The geometry of this {@code Model}.
     */
    private final Face[] faces;

    /**
     * The data backing the vertex buffer of this {@code Model}.
     */
    private final Vertex[] vertices;

    /**
     * A pool of texture coordinates projected into viewspace.
     */
    private final TexCoord[] textureCoordinates;

    public Model(Set<ModelFeature> features, Face[] faces, Vertex[] vertices, TexCoord[] textureCoordinates) {
        this.features = features;
        this.faces = faces;
        this.vertices = vertices;
        this.textureCoordinates = textureCoordinates;
    }

    /**
     * Check if this {@code Model} has the given feature enabled.
     *
     * @param feature The feature to check.
     * @return {@code true} if the feature is enabled.
     */
    public boolean hasFeature(ModelFeature feature) {
        return features.contains(feature);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Face[] getFaces() {
        return faces;
    }
}
