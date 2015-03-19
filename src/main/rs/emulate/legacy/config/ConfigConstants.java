package rs.emulate.legacy.config;

/**
 * Contains constants related to the config archive.
 * 
 * @author Major
 */
public final class ConfigConstants {

	/**
	 * The extension for entries containing data.
	 */
	public static final String DATA_EXTENSION = ".dat";

	/**
	 * The opcode that indicates all of the data for a single definition has been read.
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