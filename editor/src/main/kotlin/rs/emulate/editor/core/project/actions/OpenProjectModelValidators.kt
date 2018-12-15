package rs.emulate.editor.core.project.actions

import javafx.scene.control.Control
import org.controlsfx.validation.ValidationResult
import org.controlsfx.validation.Validator
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Paths

class VfsRootValidator : Validator<String> {
    override fun apply(input: Control, value: String): ValidationResult {
        return try {
            val path = Paths.get(value)
            if (!Files.exists(path)) {
                return ValidationResult.fromError(input, "invalid path")
            }

            val root = if (Files.isDirectory(path)) path else path.parent
            val dataFile = root.resolve("main_file_cache.dat")

            if (Files.exists(dataFile)) {
                ValidationResult()
            } else {
                ValidationResult.fromError(input, "unable to find main_file_cache.data in selected path")
            }
        } catch (_: InvalidPathException) {
            ValidationResult.fromError(input, "invalid path")
        }
    }

}
