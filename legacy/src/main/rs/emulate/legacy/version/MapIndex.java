package rs.emulate.legacy.version;

/**
 * Represents the map_index entry in the versionlist archive, used for mapping region coordinates to landscape and map
 * data files.
 *
 * @author sfix
 * @author Major
 */
public final class MapIndex {

	/**
	 * The area ids.
	 */
	private final int[] areas;

	/**
	 * The landscape file ids.
	 */
	private final int[] landscapes;

	/**
	 * The map file ids.
	 */
	private final int[] maps;

	/**
	 * The members only flags.
	 */
	private final boolean[] members;

	/**
	 * Creates the MapIndex.
	 *
	 * @param areas The area ids.
	 * @param landscapes The landscape file ids.
	 * @param maps The map file ids.
	 * @param members The members-only flags.
	 */
	public MapIndex(int[] areas, int[] landscapes, int[] maps, boolean[] members) {
		this.areas = areas.clone();
		this.landscapes = landscapes.clone();
		this.maps = maps.clone();
		this.members = members.clone();
	}

	/**
	 * Gets the area ids. The returned array will be a deep copy.
	 *
	 * @return The area ids.
	 */
	public int[] getAreas() {
		return areas.clone();
	}

	/**
	 * Gets the landscape file ids. The returned array will be a deep copy.
	 *
	 * @return The landscape file ids.
	 */
	public int[] getLandscapes() {
		return landscapes.clone();
	}

	/**
	 * Gets the map file ids. The returned array will be a deep copy.
	 *
	 * @return The map file ids.
	 */
	public int[] getMaps() {
		return maps.clone();
	}

	/**
	 * Gets the members-only area flags. The returned array will be a deep copy.
	 *
	 * @return The members-only flags.
	 */
	public boolean[] getMembers() {
		return members.clone();
	}

}