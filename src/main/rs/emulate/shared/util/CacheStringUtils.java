package rs.emulate.shared.util;

/**
 * Contains string-related utility methods.
 * 
 * @author Graham
 */
public final class CacheStringUtils {

	/**
	 * The modified set of 'extended ASCII' characters used by the client.
	 */
	public static final char[] CHARACTERS = { '\u20AC', '\0', '\u201A', '\u0192', '\u201E', '\u2026', '\u2020', '\u2021', '\u02C6',
			'\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017D', '\0', '\0', '\u2018', '\u2019', '\u201C', '\u201D', '\u2022',
			'\u2013', '\u2014', '\u02DC', '\u2122', '\u0161', '\u203A', '\u0153', '\0', '\u017E', '\u0178' };

	/**
	 * Hashes the specified string.
	 * 
	 * @param string The string.
	 * @return The hash.
	 */
	public static int hash(String string) {
		return string.hashCode();
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private CacheStringUtils() {

	}

}