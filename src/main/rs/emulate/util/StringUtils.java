package rs.emulate.util;



/**
 * Contains string-related utility methods.
 * 
 * @author Major
 */
public final class StringUtils {

	/**
	 * Camelcases the specified string.
	 *
	 * @param string The string.
	 * @return The camelcased string.
	 */
	public static String camelcase(String string) {
		string = string.toLowerCase();

		while (string.contains("-") || string.contains("_")) {
			int index = string.indexOf("-");
			if (index == -1) {
				index = string.indexOf("_");
			}

			String first = string.substring(0, index), second = string.substring(index + 1);
			string = first + Character.toUpperCase(second.charAt(0)) + second.substring(1);
		}

		return string;
	}

	/**
	 * Returns a new string consisting of the characters in the specified string, with the first letter capitalised, and
	 * the rest lower-case.
	 *
	 * @param string The string.
	 * @return The capitalised string.
	 */
	public static String capitalise(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private StringUtils() {

	}

}