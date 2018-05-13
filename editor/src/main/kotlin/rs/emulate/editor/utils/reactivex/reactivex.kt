import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.rxjavafx.sources.Change
import io.reactivex.rxjavafx.subscriptions.JavaFxSubscriptions
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue

fun <T> nullableChangesOf(fxObservable: ObservableValue<T>): Observable<Change<T>> {
    return Observable.create { emitter: ObservableEmitter<Change<T>> ->
        val listener = ChangeListener<T> { _, prev, current ->
            emitter.onNext(Change(prev, current))
        }

        fxObservable.addListener(listener)
        emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread { fxObservable.removeListener(listener) })
    }
}

