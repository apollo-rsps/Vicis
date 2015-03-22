package rs.emulate.legacy.config;

/**
 * Contains constants related to entries in the config archive.
 * 
 * @author Major
 */
public final class ConfigConstants {

	/**
	 * The extension for entries containing data.
	 */
	public static final String DATA_EXTENSION = ".dat";

	/**
	 * The default value for depth and breadth scales.
	 */
	public static final int DEFAULT_SCALE = 128;

	/**
	 * The value that indicates all of the data for a single ConfigDefinition has been read.
	 */
	public static final int DEFINITION_TERMINATOR = 0;

	/**
	 * The extension for entries containing indices.
	 */
	public static final String INDEX_EXTENSION = ".idx";

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private ConfigConstants() {

	}

}