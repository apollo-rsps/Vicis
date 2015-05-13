package rs.emulate.legacy.version;

/**
 * A type of a {@link CrcList} of a {@link VersionList} that is present in the cache by default.
 *
 * @author Major
 */
enum StandardVersionEntryType implements VersionEntryType {

	/**
	 * The StandardVersionEntryType for models.
	 */
	MODEL("model"),

	/**
	 * The StandardVersionEntryType for animations.
	 */
	ANIMATION("anim"),

	/**
	 * The StandardVersionEntryType for music.
	 */
	MUSIC("music"),

	/**
	 * The StandardVersionEntryType for maps.
	 */
	MAPS("map");

	/**
	 * The name of the file this StandardVersionEntryType is decoded from.
	 */
	private final String name;

	/**
	 * Creates the StandardVersionEntryType.
	 *
	 * @param name The name of the file the StandardVersionEntryType is decoded from.
	 */
	StandardVersionEntryType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}