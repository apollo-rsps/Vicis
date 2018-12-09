package rs.emulate.util

/**
 * Contains String-related utility methods.
 */
object CacheStringUtils {

    /**
     * The modified set of 'extended ASCII' characters used by the client.
     */
    val CHARACTERS = charArrayOf('\u20AC', '\u0000', '\u201A', '\u0192', '\u201E', '\u2026', '\u2020', '\u2021',
        '\u02C6', '\u2030', '\u0160', '\u2039', '\u0152', '\u0000', '\u017D', '\u0000', '\u0000', '\u2018', '\u2019',
        '\u201C', '\u201D', '\u2022', '\u2013', '\u2014', '\u02DC', '\u2122', '\u0161', '\u203A', '\u0153', '\u0000',
        '\u017E', '\u0178')

    /**
     * Hashes the specified string.
     */
    fun hash(string: String): Int {
        return string.hashCode()
    }

}
