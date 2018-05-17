package rs.emulate.scene3d.material

import java.nio.file.Files
import java.nio.file.Path

//@todo - crap
sealed class ShaderSource(val key: String) {
    data class Classpath(val path: String) : ShaderSource(path) {
        override fun load(): String {
            val input = ShaderSource::class.java.getResourceAsStream(path)
            val reader = input.bufferedReader()

            try {
                return reader.readText()
            } finally {
                input.close()
            }
        }
    }

    data class File(val path: Path) : ShaderSource(path.toString()) {
        override fun load() = String(Files.readAllBytes(path))
    }

    abstract fun load(): String
}
