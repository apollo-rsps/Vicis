package rs.emulate.shared.world;

/**
 * Represents a position in the world.
 *
 * @author Graham
 */
public final class Position {

	/**
	 * The number of height levels.
	 */
	public static final int HEIGHT_LEVELS = 4;

	/**
	 * The maximum distance players/NPCs can 'see'.
	 */
	public static final int MAX_DISTANCE = 15;

	/**
	 * The height level.
	 */
	private final int height;

	/**
	 * The x coordinate.
	 */
	private final int x;

	/**
	 * The z coordinate.
	 */
	private final int z;

	/**
	 * Creates the Position with the default height level.
	 *
	 * @param x The x coordinate.
	 * @param z The z coordinate.
	 */
	public Position(int x, int z) {
		this(x, z, 0);
	}

	/**
	 * Creates the Position with the specified height level.
	 *
	 * @param x The x coordinate.
	 * @param z The z coordinate.
	 * @param level The level.
	 */
	public Position(int x, int z, int level) {
		if (level < 0 || level >= HEIGHT_LEVELS) {
			throw new IllegalArgumentException("Height level out of bounds.");
		}

		this.x = x;
		this.z = z;
		this.height = level;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position other = (Position) obj;
			return height == other.height && x == other.x && z == other.z;
		}

		return false;
	}

	/**
	 * Gets the x coordinate of the central sector.
	 *
	 * @return The x coordinate of the central sector.
	 */
	public int getCentralSectorX() {
		return x / 8;
	}

	/**
	 * Gets the z coordinate of the central sector.
	 *
	 * @return The z coordinate of the central sector.
	 */
	public int getCentralSectorZ() {
		return z / 8;
	}

	/**
	 * Gets the height level.
	 *
	 * @return The height level.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the x coordinate inside the sector of this position.
	 *
	 * @return The local x coordinate.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local x coordinate inside the sector of the {@code base} position.
	 *
	 * @param base The base position.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position base) {
		return x - base.getTopLeftSectorX() * 8;
	}

	/**
	 * Gets the z coordinate inside the sector of this position.
	 *
	 * @return The local z coordinate.
	 */
	public int getLocalZ() {
		return getLocalZ(this);
	}

	/**
	 * Gets the local z coordinate inside the sector of the {@code base} position.
	 *
	 * @param base The base position.
	 * @return The local z coordinate.
	 */
	public int getLocalZ(Position base) {
		return z - base.getTopLeftSectorZ() * 8;
	}

	/**
	 * Gets the x coordinate of the sector this position is in.
	 *
	 * @return The sector x coordinate.
	 */
	public int getTopLeftSectorX() {
		return x / 8 - 6;
	}

	/**
	 * Gets the z coordinate of the sector this position is in.
	 *
	 * @return The sector z coordinate.
	 */
	public int getTopLeftSectorZ() {
		return z / 8 - 6;
	}

	/**
	 * Gets the x coordinate.
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the z coordinate.
	 *
	 * @return The z coordinate.
	 */
	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		return (height & 0x3) << 30 | (z & 0x7FFF) << 15 | x & 0x7FFF;
	}

	@Override
	public String toString() {
		return Position.class.getName() + " [x=" + x + ", y=" + z + ", height=" + height + "]";
	}

}