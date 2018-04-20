package rs.emulate.legacy.version

/**
 * A type of a [CrcList] of a [VersionList] that is present in the cache by default.
 */
internal enum class StandardVersionEntryType(override val fileName: String) : VersionEntryType {

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
    MAPS("map")

}
