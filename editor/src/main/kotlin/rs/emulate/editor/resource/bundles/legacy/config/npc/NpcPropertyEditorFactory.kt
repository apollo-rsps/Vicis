package rs.emulate.editor.resource.bundles.legacy.config.npc

import org.controlsfx.property.editor.Editors
import org.controlsfx.property.editor.PropertyEditor
import rs.emulate.editor.ui.workspace.components.propertysheet.MutablePropertyEditorFactory
import rs.emulate.editor.ui.workspace.components.propertysheet.MutablePropertyItem

object NpcPropertyEditorFactory : MutablePropertyEditorFactory<NpcProperty>() {

    override fun create(item: MutablePropertyItem<*, NpcProperty>): PropertyEditor<*>? {
        return when (item.propertyMetadata) {
            NpcProperty.Models -> null
            NpcProperty.Name -> Editors.createTextEditor(item)
            NpcProperty.Description -> Editors.createTextEditor(item)
            NpcProperty.Size -> Editors.createNumericEditor(item)
            NpcProperty.StandingSequence -> Editors.createNumericEditor(item)
            NpcProperty.WalkingSequence -> Editors.createNumericEditor(item)
            NpcProperty.MovementSequences -> null
            NpcProperty.Action1 -> Editors.createTextEditor(item)
            NpcProperty.Action2 -> Editors.createTextEditor(item)
            NpcProperty.Action3 -> Editors.createTextEditor(item)
            NpcProperty.Action4 -> Editors.createTextEditor(item)
            NpcProperty.Action5 -> Editors.createTextEditor(item)
            NpcProperty.Colours -> null
            NpcProperty.WidgetModels -> null
            NpcProperty.VisibleOnMinimap -> Editors.createCheckEditor(item)
            NpcProperty.CombatLevel -> Editors.createNumericEditor(item)
            NpcProperty.PlanarScale -> Editors.createNumericEditor(item)
            NpcProperty.VerticalScale -> Editors.createNumericEditor(item)
            NpcProperty.PriorityRender -> Editors.createCheckEditor(item)
            NpcProperty.Brightness -> Editors.createNumericEditor(item)
            NpcProperty.Diffusion -> Editors.createNumericEditor(item)
            NpcProperty.HeadIcon -> Editors.createNumericEditor(item)
            NpcProperty.DefaultOrientation -> Editors.createNumericEditor(item)
            NpcProperty.Morphisms -> null
            NpcProperty.Clickable -> Editors.createCheckEditor(item)
        }
    }

}
