package rs.emulate.editor.startup

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.nio.file.Path
import java.nio.file.Paths



class EditorStartupModel : ViewModel() {
    companion object {
        private const val DATA_FILE = "cache-data-file"
        private val DEFAULT_DIR = System.getProperty("user.home")
    }

    val cacheDataFileProperty = SimpleObjectProperty(Paths.get(config.string(DATA_FILE, DEFAULT_DIR)))
    var cacheDataFile : Path by cacheDataFileProperty

    val cacheTypeProperty = SimpleObjectProperty(EditorCacheType.Legacy)
    var cacheType : EditorCacheType by cacheTypeProperty

    override fun onCommit() {
        with(config) {
            set(DATA_FILE, cacheDataFile)
            save()
        }
    }
}
