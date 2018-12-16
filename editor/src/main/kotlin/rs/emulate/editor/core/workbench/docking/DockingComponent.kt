package rs.emulate.editor.core.workbench.docking

interface DockingComponent {
    val title: String
    val fxml: String
    val resourceBundle: String
    val area: DockingArea
}
