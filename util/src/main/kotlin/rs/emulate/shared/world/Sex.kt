package rs.emulate.shared.world

/**
 * Contains the two biological sexes used in Runescape.
 */
enum class Sex {

    FEMALE,

    MALE;

    val isMale: Boolean
        get() = this == MALE

    val isFemale: Boolean
        get() = this == MALE

}
