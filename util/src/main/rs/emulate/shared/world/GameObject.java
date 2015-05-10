package rs.emulate.shared.world;

/**
 * Represents an object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class GameObject {

	/**
	 * The config value that stores the object's id, type, and orientation.
	 */
	private final int config;

	/**
	 * The position of the object, packed into an integer to reduce memory usage.
	 */
	private final int position;

	/**
	 * Creates a game object.
	 * 
	 * @param id The object's id.
	 * @param position The position.
	 * @param type The type code of the object.
	 * @param orientation The orientation of the object.
	 */
	public GameObject(int id, Position position, int type, int orientation) {
		this.position = position.getHeight() << 30 | position.getX() << 15 | position.getZ();
		this.config = (id << 8) + (type << 2) + orientation;
	}

	/**
	 * Gets this object's id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return config >> 8;
	}

	/**
	 * Gets the {@link Position} of this object.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return new Position((position >> 15) & 0x7FFF, position & 0x7FFF, position >> 30);
	}

	/**
	 * Gets this object's orientation.
	 * 
	 * @return The orientation.
	 */
	public int getRotation() {
		return config & 0x3;
	}

	/**
	 * Gets this object's type.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return (config >> 2) & 0x3F;
	}

	@Override
	public String toString() {
		return GameObject.class.getName() + " [id=" + getId() + ", type=" + getType() + ", rotation=" + getRotation() + "]";
	}

}