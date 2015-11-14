package rs.emulate.legacy.version;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Contains data used for mapping region coordinates to object and map files.
 *
 * @author sfix
 * @author Major
 */
public final class MapIndex {

	/**
	 * The name of the archive entry containing the map index data.
	 */
	public static final String ENTRY_NAME = "map_index";

	/**
	 * Creates a new MapIndex.
	 *
	 * @param areas The area ids, of the form {@code region_x << 8 | region_y}. Must not be {@code null}.
	 * @param objects The object file ids. Must not be {@code null}.
	 * @param maps The map file ids. Must not be {@code null}.
	 * @param members The members-only flags. Must not be {@code null}.
	 * @return The MapIndex.
	 * @throws NullPointerException If any of the parameters are {@code null}.
	 */
	public static MapIndex create(int[] areas, int[] objects, int[] maps, boolean[] members) {
		Objects.requireNonNull(areas, "Area ids must not be null.");
		Objects.requireNonNull(objects, "Object file ids must not be null.");
		Objects.requireNonNull(maps, "Map file ids must not be null.");
		Objects.requireNonNull(members, "Members flags must not be null.");

		int expected = members.length;
		Preconditions.checkArgument(expected == areas.length && expected == objects.length && expected == maps.length,
				"MapIndex arrays must be of equal length.");

		return new MapIndex(areas, objects, maps, members);
	}

	/**
	 * The area ids, of the form {@code region_x << 8 | region_y}.
	 */
	private final int[] areas;

	/**
	 * The map file ids.
	 */
	private final int[] maps;

	/**
	 * The members only flags.
	 */
	private final boolean[] members;

	/**
	 * The object file ids.
	 */
	private final int[] objects;

	/**
	 * Creates the MapIndex.
	 *
	 * @param areas The area ids, of the form {@code region_x << 8 | region_y}. Must not be {@code null}.
	 * @param objects The object file ids. Must not be {@code null}.
	 * @param maps The map file ids. Must not be {@code null}.
	 * @param members The members-only flags. Must not be {@code null}.
	 * @throws NullPointerException If any of the parameters are {@code null}.
	 */
	public MapIndex(int[] areas, int[] objects, int[] maps, boolean[] members) {
		this.areas = areas.clone();
		this.objects = objects.clone();
		this.maps = maps.clone();
		this.members = members.clone();
	}

	/**
	 * Gets the area ids, of the form {@code region_x << 8 | region_y}. The returned array will be a deep copy.
	 *
	 * @return The area ids.
	 */
	public int[] getAreas() {
		return areas.clone();
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

	/**
	 * Gets the object file ids. The returned array will be a deep copy.
	 *
	 * @return The object file ids.
	 */
	public int[] getObjects() {
		return objects.clone();
	}

	/**
	 * Gets the amount of elements in this MapIndex
	 *
	 * @return The size.
	 */
	public int getSize() {
		return areas.length;
	}

}