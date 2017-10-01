package rs.emulate.legacy.model;

/**
 * Standard {@code UV} texture coordinates projected into viewspace.
 */
public class TexCoord {
    private final int origin;
    private final int u;
    private final int v;

    public TexCoord(int origin, int u, int v) {
        this.origin = origin;
        this.u = u;
        this.v = v;
    }

    public int getOrigin() {
        return origin;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }
}
