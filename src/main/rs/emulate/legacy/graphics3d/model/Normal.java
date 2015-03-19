package rs.emulate.legacy.graphics3d.model;

/**
 * A vertex normal.
 *
 * @author Major
 */
public final class Normal {

	/**
	 * The magnitude of this Normal.
	 */
	private final int magnitude;

	/**
	 * The x point of this Normal.
	 */
	private final int x;

	/**
	 * The y point of this Normal.
	 */
	private final int y;

	/**
	 * The z point of this Normal.
	 */
	private final int z;

	/**
	 * Creates the Normal.
	 *
	 * @param x The x point of the Normal.
	 * @param y The y point of the Normal.
	 * @param z The z point of the Normal.
	 * @param magnitude The magnitude of the Normal.
	 */
	public Normal(int x, int y, int z, int magnitude) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.magnitude = magnitude;
	}

	/**
	 * Gets the magnitude of this Normal.
	 *
	 * @return The magnitude.
	 */
	public int getMagnitude() {
		return magnitude;
	}

	/**
	 * Gets the x point of this Normal.
	 *
	 * @return The x point.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y point of this Normal.
	 *
	 * @return The y point.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z point of this Normal.
	 *
	 * @return The z point.
	 */
	public int getZ() {
		return z;
	}

}