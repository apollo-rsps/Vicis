package rs.emulate.editor.core.workbench.docking

interface DockingComponent {
    val title: String
    val fxml: String
    val area: DockingArea
}
