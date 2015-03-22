package rs.emulate.legacy.map.collision;

/**
 * A type of flag in a {@link CollisionMatrix}.
 *
 * @author Major
 */
public enum CollisionFlag {

	/**
	 * The walk north flag.
	 */
	MOB_NORTH(0),

	/**
	 * The walk east flag.
	 */
	MOB_EAST(1),

	/**
	 * The walk south flag.
	 */
	MOB_SOUTH(2),

	/**
	 * The walk west flag.
	 */
	MOB_WEST(3),

	/**
	 * The projectile north flag.
	 */
	PROJECTILE_NORTH(4),

	/**
	 * The projectile east flag.
	 */
	PROJECTILE_EAST(5),

	/**
	 * The projectile south flag.
	 */
	PROJECTILE_SOUTH(6),

	/**
	 * The projectile west flag.
	 */
	PROJECTILE_WEST(7);

	/**
	 * The index of the bit this flag is stored in.
	 */
	private final int bit;

	/**
	 * Creates the CollisionFlag.
	 *
	 * @param bit The index of the bit this flag is stored in.
	 */
	private CollisionFlag(int bit) {
		this.bit = bit;
	}

	/**
	 * Gets this CollisionFlag, as a {@code byte}.
	 * 
	 * @return The value, as a {@code byte}.
	 */
	public byte asByte() {
		return (byte) (1 << bit);
	}

	/**
	 * Gets the index of the bit this flag is stored in.
	 * 
	 * @return The index of the bit.
	 */
	public int getBit() {
		return bit;
	}

}