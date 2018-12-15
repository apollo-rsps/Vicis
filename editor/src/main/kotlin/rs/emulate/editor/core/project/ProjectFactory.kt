package rs.emulate.editor.core.project

import java.nio.file.Path
import javax.inject.Named

interface ProjectFactory {

    @Named("legacy")
    fun createLegacyProject(name: String, vfsRoot: Path): LegacyProject

    @Named("modern")
    fun createModernProject(name: String, vfsRoot: Path): ModernProject

}

// XXX: Guice doesn't like it when this is a default interface method.
fun ProjectFactory.createProject(name: String, vfsRoot: Path, ty: ProjectType) = when (ty) {
    ProjectType.Legacy -> createLegacyProject(name, vfsRoot)
    ProjectType.Modern -> createModernProject(name, vfsRoot)
}
