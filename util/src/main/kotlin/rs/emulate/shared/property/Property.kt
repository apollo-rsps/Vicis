package rs.emulate.shared.property

import java.util.Objects

/**
 * A mutable property belonging to some sort of definition.
 *
 * @param V The type of the value.
 * @param T The type of [PropertyType].
 */
open class Property<V, out T : PropertyType> {

    /**
     * @param value The current value.
     * @param default The default value.
     */
    constructor(type: T, value: V, default: V) {
        this.type = type
        this.default = default
        this.value = value
    }

    /**
     * @param default The default value.
     */
    constructor(type: T, default: V) {
        this.type = type
        this.default = default
        value = default
    }

    /**
     * The value of this DefinitionProperty.
     */
    var value: V

    val type: T
    val default: V

    /**
     * Gets the name of this DefinitionProperty.
     */
    val name: String
        get() = type.formattedName()

    /**
     * Resets the value of this DefinitionProperty.
     */
    fun reset() {
        value = default
    }

    /**
     * Returns whether or not there is a custom value present.
     */
    fun valuePresent(): Boolean {
        return value != null
    }

    override fun equals(other: Any?): Boolean {
        if (other is Property<*, *>) {
            return type == other.type && value == other.value
        }

        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(type, value)
    }

    override fun toString(): String {
        val value = value

        return when (value) {
            is BooleanArray -> value.contentToString()
            is CharArray -> value.contentToString()
            is ByteArray -> value.contentToString()
            is ShortArray -> value.contentToString()
            is IntArray -> value.contentToString()
            is LongArray -> value.contentToString()
            is FloatArray -> value.contentToString()
            is DoubleArray -> value.contentToString()
            is Array<*> -> value.contentDeepToString()
            else -> value.toString()
        }
    }

}
