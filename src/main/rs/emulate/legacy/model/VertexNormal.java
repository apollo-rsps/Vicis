package rs.emulate.legacy.model;

/**
 * A vertex normal.
 *
 * @author Major
 */
public final class VertexNormal {

	/**
	 * The amount of faces that have this VertexNormal.
	 */
	private final int faces;

	/**
	 * The x point of this VertexNormal.
	 */
	private final int x;

	/**
	 * The y point of this VertexNormal.
	 */
	private final int y;

	/**
	 * The z point of this VertexNormal.
	 */
	private final int z;

	/**
	 * Creates the VertexNormal.
	 *
	 * @param x The x point of the VertexNormal.
	 * @param y The y point of the VertexNormal.
	 * @param z The z point of the VertexNormal.
	 * @param faces The amount of faces that have this VertexNormal.
	 */
	public VertexNormal(int x, int y, int z, int faces) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.faces = faces;
	}

	/**
	 * Gets the amount of faces that have this VertexNormal.
	 *
	 * @return The amount of faces.
	 */
	public int getFaceCount() {
		return faces;
	}

	/**
	 * Gets the x point of this VertexNormal.
	 *
	 * @return The x point.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y point of this VertexNormal.
	 *
	 * @return The y point.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z point of this VertexNormal.
	 *
	 * @return The z point.
	 */
	public int getZ() {
		return z;
	}

}