package rs.emulate.editor.utils.javafx

import javafx.beans.binding.Bindings
import javafx.beans.binding.DoubleBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ObservableValue
import java.util.concurrent.Callable

fun <S, T> ObservableValue<S>.objectBinding(mapper: (S) -> T): ObjectBinding<T> {
    return Bindings.createObjectBinding<T>(Callable { mapper(value) }, this)
}

fun <S> ObservableValue<S>.doubleBinding(mapper: (S) -> Double): DoubleBinding {
    return Bindings.createDoubleBinding(Callable { mapper(value) }, this)
}
