package rs.emulate.editor.utils.javafx

import javafx.beans.value.ObservableValue
import java.util.concurrent.atomic.AtomicBoolean


fun <T> ObservableValue<T>.onGuardedChange(op: (T?) -> Unit) = apply {
    val changingGuard = AtomicBoolean(false)

    addListener { o, oldValue, newValue ->
        if (changingGuard.compareAndSet(false, true)) {
            try {
                op(newValue)
            } finally {
                changingGuard.set(false)
            }
        }
    }
}

fun <T> ObservableValue<T>.addGuardedListener(op: (ObservableValue<out T>, T?, T?) -> Unit) = apply {
    val changingGuard = AtomicBoolean(false)

    addListener { o, oldValue, newValue ->
        if (changingGuard.compareAndSet(false, true)) {
            try {
                op(o, oldValue, newValue)
            } finally {
                changingGuard.set(false)
            }
        }
    }
}


