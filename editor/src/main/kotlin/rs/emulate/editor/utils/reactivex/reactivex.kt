import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.rxjavafx.sources.Change
import io.reactivex.rxjavafx.subscriptions.JavaFxSubscriptions
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node

fun <T> nullableChangesOf(fxObservable: ObservableValue<T>): Observable<Change<T>> {
    return Observable.create { emitter: ObservableEmitter<Change<T>> ->
        val listener = ChangeListener<T> { _, prev, current ->
            emitter.onNext(Change(prev, current))
        }

        fxObservable.addListener(listener)
        emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread { fxObservable.removeListener(listener) })
    }
}

fun <T : Event> Node.filteredEvents(eventType: EventType<T>): Observable<T> {
    return Observable.create { emitter: ObservableEmitter<T> ->
        val handler = EventHandler<T> { emitter.onNext(it) }

        addEventFilter(eventType, handler)

        emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread { removeEventHandler(eventType, handler) })
    }.subscribeOn(JavaFxScheduler.platform())
}
