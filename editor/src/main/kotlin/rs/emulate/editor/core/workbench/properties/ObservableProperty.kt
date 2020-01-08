package rs.emulate.editor.core.workbench.properties

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue

abstract class ObservableProperty<T>(
    val property: ResourceProperty,
    initialValue: T
) : ObservableValue<T>, WritableValue<T> {
    protected val delegate = SimpleObjectProperty<T>(null, property.name, initialValue)

    final override fun getValue(): T = delegate.value
    final override fun setValue(value: T) = delegate.set(value)

    final override fun addListener(listener: ChangeListener<in T>) = delegate.addListener(listener)
    final override fun addListener(listener: InvalidationListener) = delegate.addListener(listener)

    final override fun removeListener(listener: ChangeListener<in T>) = delegate.removeListener(listener)
    final override fun removeListener(listener: InvalidationListener) = delegate.removeListener(listener)

    open fun setValue(value: Any?): Boolean {
        val casted = value as? T ?: return false

        delegate.set(casted)
        return true
    }

    protected companion object {

        /**
         * Attempts to derive an [Int] from the provided [Any].
         *
         * Returns:
         * - [default] if [this] is `null`, or a [blank][isBlank] string.
         * - [IllegalInput.Unparseable], if the [trimmed][trim] string could not be parsed as an [Int].
         * - [IllegalInput.ValueOutOfBounds], if [this] could be interpreted as an [Int], but exceeded either the [min]
         *   or the [max] bound.
         *
         * @throws IllegalArgumentException If [this] was not `null`, an [Int], or a [String].
         */
        fun <T : Int?> Any?.tryParseInt(default: T, min: Int = 0, max: Int = Int.MAX_VALUE): Result<T, IllegalInput<Int>> {
            val int = when (this) {
                null -> return Ok(default)
                is Int -> this
                is String -> {
                    val trimmed = trim()
                    if (trimmed.isEmpty()) {
                        return Ok(default)
                    }

                    trimmed.toIntOrNull() ?: return Err(IllegalInput.Unparseable(trimmed))
                }
                else -> throw IllegalArgumentException("Unsupported type ${this::class.simpleName}.")
            }

            @Suppress("UNCHECKED_CAST")
            return when {
                int < min || int > max -> Err(IllegalInput.ValueOutOfBounds(int, (min + 1) until max))
                else -> Ok(int as T)
            }
        }

        sealed class IllegalInput<T> {
            data class ValueOutOfBounds<T : Comparable<T>>(val value: T, val max: ClosedRange<T>) : IllegalInput<T>()
            data class Unparseable<T>(val input: String) : IllegalInput<T>()
        }
    }

}
