package rs.emulate.editor.core.project

import com.google.inject.assistedinject.Assisted
import rs.emulate.editor.vfs.ModernFileId
import rs.emulate.editor.vfs.ModernResourceType
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.index.VirtualFileIndexer
import rs.emulate.modern.fs.FileStore
import java.nio.file.Path
import javax.inject.Inject


class ModernProject @Inject constructor(@Assisted override val name: String, @Assisted vfsRoot: Path) :
    Project<ModernFileId, ModernResourceType> {

    private val fileStore = FileStore.open(vfsRoot)

    override fun createIndexer(): VirtualFileIndexer<out VirtualFileId, out ResourceType> {
        TODO("unimplemented")
    }
}
