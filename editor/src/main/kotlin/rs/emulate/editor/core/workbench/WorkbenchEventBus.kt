package rs.emulate.editor.core.workbench

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkbenchEventBus @Inject constructor() {
    val bus: BroadcastChannel<Any> = ConflatedBroadcastChannel<Any>()

    fun send(o: Any) {
        GlobalScope.launch(Dispatchers.Main) {
            bus.send(o)
        }
    }

    inline fun <reified T> asChannel(): ReceiveChannel<T> {
        return bus.openSubscription().filter { it is T }.map { it as T }
    }
}
