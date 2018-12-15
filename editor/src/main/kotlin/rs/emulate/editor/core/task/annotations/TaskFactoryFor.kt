package rs.emulate.editor.core.task.annotations

import javafx.concurrent.Task
import kotlin.reflect.KClass

annotation class TaskFactoryFor(val type: KClass<out Task<out Any>>)
