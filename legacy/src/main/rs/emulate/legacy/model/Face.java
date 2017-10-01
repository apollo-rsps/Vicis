package rs.emulate.legacy.model;

public final class Face {

    /**
     * The colour of this triangle.
     */
    private final int colour;

    /**
     * An optional render priority of this {@code Face}.  May be {@code -1} to represent none.
     */
    private final int renderPriority;

    /**
     * An optional alpha value of this {@code Face}.  May be {@code -1} to represent none.
     */
    private final int alpha;

    /**
     * An optional identifier of a bone this {@code Face} is attached to.  May be {@code -1} to
     * represent none.
     */
    private final int bone;

    /**
     * An optional pointer to the texture used by this {@code Face}.
     */
    private final int texturePointer;

    /**
     * Index of the {@link Vertex} that represents the bottom left of this {@code Face}.
     */
    private final int a;

    /**
     * Index of the {@link Vertex} that represents the bottom right of this {@code Face}.
     */
    private final int b;

    /**
     * Index of the {@link Vertex} that represents the top of this {@code Face}.
     */
    private final int c;

    public Face(int a, int b, int c, int colour, int renderPriority, int alpha, int bone,
                int texturePointer) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.colour = colour;
        this.renderPriority = renderPriority;
        this.alpha = alpha;
        this.bone = bone;
        this.texturePointer = texturePointer;
    }

    public int getRenderPriority() {
        return renderPriority;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBone() {
        return bone;
    }

    public int getTexturePointer() {
        return texturePointer;
    }

    public int getColour() {
        return colour;
    }

    public int getA() {
        return a;
    }

    public int getC() {
        return c;
    }

    public int getB() {
        return b;
    }
}
