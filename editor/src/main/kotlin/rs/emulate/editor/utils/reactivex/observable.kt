package rs.emulate.editor.utils.reactivex

import io.reactivex.Observable.merge
import io.reactivex.rxjavafx.observables.JavaFxObservable.changesOf
import javafx.beans.value.ObservableValue

fun <T: Any> mergeChangesOf(vararg values: ObservableValue<T>) = merge(values.map { changesOf(it) })
