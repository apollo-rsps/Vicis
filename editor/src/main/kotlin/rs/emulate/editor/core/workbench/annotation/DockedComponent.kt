package rs.emulate.editor.core.workbench.annotation

import rs.emulate.editor.core.workbench.docking.DockingArea

/**
 * An annotation describing a singleton component that is docked at a given area in the workbench.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DockedComponent(val title: String, val area: DockingArea, val fxml: String)
