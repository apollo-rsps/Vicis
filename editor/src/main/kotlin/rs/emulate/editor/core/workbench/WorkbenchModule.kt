package rs.emulate.editor.core.workbench

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import rs.emulate.editor.core.workbench.docking.DockingComponent
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerDockingComponent

class WorkbenchModule(private val ctx: WorkbenchContext) : AbstractModule() {
    override fun configure() {
        binder().bind(WorkbenchContext::class.java).toInstance(ctx)

        val dockingComponentBuilder = Multibinder.newSetBinder(binder(), DockingComponent::class.java)

        dockingComponentBuilder
            .addBinding()
            .toInstance(WorkbenchExplorerDockingComponent())
    }
}
