package rs.emulate.editor.core.task

import javafx.collections.FXCollections
import javafx.concurrent.Task
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class TaskRunner {

    val tasks = FXCollections.observableArrayList<Task<*>>()
    val executor = Executors.newSingleThreadExecutor()

    suspend fun <R> run(task: Task<R>): R {
        tasks.add(task)

        try {
            CompletableFuture.runAsync(task, executor).await()
            return task.value
        } finally {
            tasks.remove(task)
        }
    }
}
