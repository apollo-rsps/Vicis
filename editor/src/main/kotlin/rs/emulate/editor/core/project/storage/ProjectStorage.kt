package rs.emulate.editor.core.project.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JSON
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

class ManagedComponent(val data: Any, val path: Path, val serializer: KSerializer<*>)

class ProjectStorage(val root: Path) {
    val managedComponents = mutableMapOf<KClass<*>, ManagedComponent>()

    fun create() {
        if (Files.notExists(root)) {
            Files.createDirectories(root)
        }
    }

    suspend inline fun <reified T : Any> load(serializer: KSerializer<T>): T = withContext(Dispatchers.IO) {
        val ty = T::class

        if (managedComponents.containsKey(ty)) {
            return@withContext managedComponents[ty] as T
        }

        val component = ty.findAnnotation<ProjectStorageComponent>() ?: throw Exception("No annotation present")
        val path = root.resolve("${component.name}.json")

        val data = if (!Files.exists(path)) {
            ty.createInstance()
        } else {
            JSON.parse(serializer, Files.readString(path))
        }

        managedComponents[ty] = ManagedComponent(data, path, serializer as KSerializer<*>)
        data
    }

    suspend fun save() {
        managedComponents.forEach { (_, component) ->
            val json = JSON.stringify(component.serializer as KSerializer<Any>, component.data)

            Files.writeString(
                component.path,
                json,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }
    }


    companion object {
        fun resolve(projectRoot: Path): ProjectStorage {
            return ProjectStorage(projectRoot.resolve(".vicis"))
        }
    }
}
