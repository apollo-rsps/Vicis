package rs.emulate.editor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import java.nio.file.Paths

@Configuration
open class CacheConfiguration {

    @Bean
    open fun fileSystem() = IndexedFileSystem(Paths.get("data/resources/377"), AccessMode.READ)

}