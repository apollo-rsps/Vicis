package rs.emulate.editor.core.project

import com.google.inject.assistedinject.Assisted
import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.LegacyResourceType
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.index.LegacyFileIndexer
import rs.emulate.editor.vfs.index.VirtualFileIndexer
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import java.nio.file.Path
import javax.inject.Inject

class LegacyProject @Inject constructor(
    @Assisted override val name: String,
    @Assisted vfsRoot: Path
) : Project<LegacyFileId, LegacyResourceType> {

    private val fs = IndexedFileSystem(vfsRoot, AccessMode.READ_WRITE)

    override fun createIndexer(): VirtualFileIndexer<out VirtualFileId, out ResourceType> {
        return LegacyFileIndexer(fs, ProjectMetadataStore())
    }
}
