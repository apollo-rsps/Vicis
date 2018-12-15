package rs.emulate.editor.core.project

import rs.emulate.editor.core.project.tasks.LoadProjectIndexTask
import rs.emulate.editor.core.project.tasks.LoadProjectIndexTaskFactory
import rs.emulate.editor.core.task.TaskRunner
import rs.emulate.editor.vfs.LegacyFileLoader
import rs.emulate.editor.vfs.index.LegacyFileIndexer
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

//}

class ProjectLoader @Inject constructor(val taskRunner: TaskRunner, val taskFactory: LoadProjectIndexTaskFactory) {
    suspend fun loadProject(name: String, vfsPath: Path, ty: ProjectType): Project {
        val vfsRoot = if (!Files.isDirectory(vfsPath)) vfsPath.parent else vfsPath
        val storagePath = vfsRoot.resolve(".vicis")

        if (Files.notExists(storagePath)) {
            Files.createDirectories(storagePath)
        }

        val storage = ProjectStorage(storagePath)

        val (loader, indexer) = when (ty) {
            ProjectType.Legacy -> {
                val cache = IndexedFileSystem(vfsRoot, AccessMode.READ_WRITE)
                val loader = LegacyFileLoader(cache)
                val indexer = LegacyFileIndexer(cache, ProjectMetadataStore())

                loader to indexer
            }
            ProjectType.Modern -> {
                TODO("Unsupported")
            }
        }

        val index = taskRunner.run(LoadProjectIndexTask(storage, indexer))

        return Project(name, loader, index)
    }
}
