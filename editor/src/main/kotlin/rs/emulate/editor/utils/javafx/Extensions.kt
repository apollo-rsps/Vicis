package rs.emulate.editor.utils.javafx

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor


fun <E : Event> createAsyncEventHandler(handler: suspend (E) -> Unit): EventHandler<E> {
    val actor = GlobalScope.actor<E>(Dispatchers.Main, capacity = Channel.CONFLATED) {
        for (event in channel) {
            handler(event)
        }
    }

    return EventHandler { actor.offer(it) }
}

fun <E, F> bindWithMapping(src: ObservableList<F>, dest: ObservableList<E>, mapper: (F) -> E) {
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
