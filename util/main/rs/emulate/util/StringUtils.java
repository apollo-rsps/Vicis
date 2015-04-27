package rs.emulate.util;

import java.util.StringJoiner;

/**
 * Contains String-related utility methods.
 * 
 * @author Major
 */
public final class StringUtils {

	/**
	 * Returns a new string consisting of the characters in the specified String, with the first letter of each word
	 * capitalised, and the rest lower-case.
	 *
	 * @param string The String.
	 * @return The capitalised String.
	 */
	public static String capitalise(String string) {
		String[] words = string.split(" ");
		StringJoiner builder = new StringJoiner(" ");

		for (String word : words) {
			String capitalised = Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
			builder.add(capitalised);
		}

		return builder.toString();
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private StringUtils() {

	}

}