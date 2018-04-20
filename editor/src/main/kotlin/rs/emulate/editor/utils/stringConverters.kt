package rs.emulate.editor.utils

import javafx.util.StringConverter
import java.nio.file.Path
import java.nio.file.Paths

class PathConverter : StringConverter<Path>() {
    override fun toString(path: Path) = path.toString()
    override fun fromString(value: String) = Paths.get(value)
}
