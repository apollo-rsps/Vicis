package rs.emulate.shared.property

/**
 * A type of a property.
 */
interface PropertyType {

    /**
     * Gets the name of this PropertyType, capitalised and with underscores ('_') replaced with spaces (' ').
     */
    fun formattedName(): String {
        return capitalise(name, split = "_")
    }

    /**
     * The name of the property.
     */
    val name: String

    companion object {

        /**
         * Returns a new string consisting of the characters in the specified String, with the first letter of each word
         * capitalised, and the rest lower-case.
         */
        private fun capitalise(string: String, split: String = " "): String {
            return string.split(split.toRegex())
                .dropLastWhile(String::isEmpty)
                .joinToString(" ") {
                    it[0].toUpperCase() + it.substring(1).toLowerCase()
                }
        }

    }

}
