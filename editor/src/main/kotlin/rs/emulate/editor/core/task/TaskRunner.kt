package rs.emulate.editor.core.task

import javafx.concurrent.Task
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.function.Supplier

class TaskRunner(val threadPool: ExecutorService) {
    suspend fun <R> run(task: Task<R>) = CompletableFuture.supplyAsync(Supplier { task.get() }, threadPool).await()
}
