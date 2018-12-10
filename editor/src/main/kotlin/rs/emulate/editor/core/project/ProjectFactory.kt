package rs.emulate.editor.core.project

import java.nio.file.Path
import javax.inject.Named

interface ProjectFactory {

    @Named("legacy") fun createLegacyProject(vfsRoot: Path)

    @Named("modern") fun createModernProject(vfsRoot: Path)

}
