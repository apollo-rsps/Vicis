package rs.emulate.editor.core.project.tasks

import com.google.inject.assistedinject.Assisted
import javafx.collections.FXCollections
import javafx.concurrent.Task
import rs.emulate.editor.core.project.ProjectIndex
import rs.emulate.editor.core.project.ProjectIndexCategory
import rs.emulate.editor.core.project.ProjectIndexEntry
import rs.emulate.editor.core.project.storage.ProjectStorage
import rs.emulate.editor.core.task.annotations.TaskFactoryFor
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.index.VirtualFileIndexer
import javax.inject.Inject

@TaskFactoryFor(type = LoadProjectIndexTask::class)
interface LoadProjectIndexTaskFactory {
    fun create(
        storage: ProjectStorage,
        indexer: VirtualFileIndexer<out VirtualFileId, out ResourceType>
    ): LoadProjectIndexTask
}

class LoadProjectIndexTask @Inject constructor(
    @Assisted val storage: ProjectStorage,
    @Assisted val indexer: VirtualFileIndexer<out VirtualFileId, out ResourceType>
) : Task<ProjectIndex<*>>() {
    override fun call(): ProjectIndex<*> {
        updateProgress(0.0, 1.0)

        val indexList = indexer.index()

        updateProgress(0.8, 1.0)

        val indexCategories = FXCollections.observableArrayList(indexList.map { index ->
            ProjectIndexCategory(index.category, FXCollections.observableArrayList(
                index.entries.map { ProjectIndexEntry(it.id, it.name) }
            ))
        })

        updateProgress(1.0, 1.0)

        return ProjectIndex(indexCategories)
    }
}
