package rs.emulate.editor.core

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Names
import rs.emulate.editor.core.project.LegacyProject
import rs.emulate.editor.core.project.ModernProject
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.project.ProjectFactory
import rs.emulate.editor.core.workbench.docking.DockingComponent
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerDockingComponent

class CoreModule : AbstractModule() {
    override fun configure() {
//        install(FactoryModuleBuilder()
//            .implement(Project::class.java, Names.named("legacy"), LegacyProject::class.java)
//            .implement(Project::class.java, Names.named("modern"), ModernProject::class.java)
//            .build(ProjectFactory::class.java)
    }
}
