package rs.emulate.editor.workspace.resource.legacy

import rs.emulate.editor.workspace.resource.ObjectResourceBundle
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceBundleLoader
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import java.nio.file.Path

class LegacyResourceBundleLoader(val path: Path) : ResourceBundleLoader {
    override fun load(out: MutableList<ResourceBundle>) {
        val fs = IndexedFileSystem(path, AccessMode.READ)

        out.add(ObjectResourceBundle(fs))
    }

}
