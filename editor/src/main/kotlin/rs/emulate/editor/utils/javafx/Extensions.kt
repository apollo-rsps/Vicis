package rs.emulate.editor.utils.javafx

import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.stage.Stage
import javafx.stage.Window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

val Node.window : Window
    get() = scene.window

val Node.stage : Stage?
    get() = scene.window as? Stage

fun <T> ObservableValue<T>.onChange(callback: (T) -> Unit) = addListener { _, _, newValue -> callback(newValue) }

fun <E : Event> createAsyncEventHandler(handler: suspend (E) -> Unit): EventHandler<E> {
    val actor = GlobalScope.actor<E>(Dispatchers.Main, capacity = Channel.CONFLATED) {
        for (event in channel) {
            handler(event)
        }
    }

    return EventHandler { actor.offer(it) }
}

fun <E, F> bindWithMapping(src: ObservableList<F>, dest: ObservableList<E>, mapper: (F) -> E) {
    dest.setAll(src.map(mapper))

    src.addListener(ListChangeListener { c ->
        while (c.next()) {
            if (c.wasRemoved()) {
                for (a in c.removed) {
                    val from = c.from
                    dest.removeAt(from)
                }
            }

            if (c.wasAdded()) {
                for (a in c.addedSubList) {
                    val indexAdded = src.indexOf(a)
                    dest.add(indexAdded, mapper(a))
                }
            }
        }
    })
}

fun <E, F> ObservableList<F>.mapObservableList(mapper: (F) -> E) = MappedList(mapper, this)
