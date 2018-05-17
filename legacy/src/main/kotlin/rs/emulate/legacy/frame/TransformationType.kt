package rs.emulate.legacy.frame

enum class TransformationType(val value: Int) {
    RECENTER(0),
    TRANSLATE(1),
    ROTATE(2),
    SCALE(3),
    UNKNOWN(4),
    ALPHA(5);

    companion object {
        fun lookup(type: Int): TransformationType {
            return values().find { it.value == type }
                ?: throw IllegalArgumentException("Unrecognised transformation type $type.")
        }
    }
}
