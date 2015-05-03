package rs.emulate.shared.util;

/**
 * A point on a 2-dimensional coordinate system.
 *
 * @author Major
 */
public final class Point {

	/**
	 * The x coordinate of this Point.
	 */
	private final int x;

	/**
	 * The y coordinate of this Point.
	 */
	private final int y;

	/**
	 * Creates the Point.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x coordinate of this Point.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate of this Point.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

}