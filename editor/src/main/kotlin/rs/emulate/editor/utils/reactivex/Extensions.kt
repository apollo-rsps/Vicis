package rs.emulate.editor.utils.reactivex

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.rxjavafx.subscriptions.JavaFxSubscriptions
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node

fun <T : Event> Node.filteredEvents(eventType: EventType<T>): Observable<T> {
    return Observable.create { emitter: ObservableEmitter<T> ->
        val handler = EventHandler<T> { emitter.onNext(it) }

        addEventFilter(eventType, handler)

        emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread { removeEventHandler(eventType, handler) })
    }
}
