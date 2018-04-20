package rs.emulate.shared.property

import java.util.Arrays
import java.util.Objects

/**
 * A mutable property belonging to some sort of definition.
 *
 * @param type The [PropertyType].
 * @param value The value.
 * @param default The default value.
 * @param V The type of the value.
 * @param T The type of [PropertyType].
 */
open class Property<V : Any, out T : PropertyType>(val type: T, value: V?, val default: V?) {

    /**
     * The value of this DefinitionProperty.
     */
    var value: V? = value
        get() = field ?: default

    /**
     * Gets the name of this DefinitionProperty.
     */
    val name: String
        get() = type.formattedName()

    /**
     * Resets the value of this DefinitionProperty.
     */
    fun reset() {
        value = null
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
        val value = value ?: return "null"
        val type = value::class.java

        if (!type.isArray) {
            return value.toString()
        }

        val component = type.componentType

        return when (component) {
            java.lang.Boolean.TYPE -> Arrays.toString(value as BooleanArray)
            Character.TYPE -> Arrays.toString(value as CharArray)
            java.lang.Byte.TYPE -> Arrays.toString(value as ByteArray)
            java.lang.Short.TYPE -> Arrays.toString(value as ShortArray)
            Integer.TYPE -> Arrays.toString(value as IntArray)
            java.lang.Long.TYPE -> Arrays.toString(value as LongArray)
            java.lang.Float.TYPE -> Arrays.toString(value as FloatArray)
            java.lang.Double.TYPE -> Arrays.toString(value as DoubleArray)
            else -> Arrays.toString(value as Array<*>)
        }
    }

}
