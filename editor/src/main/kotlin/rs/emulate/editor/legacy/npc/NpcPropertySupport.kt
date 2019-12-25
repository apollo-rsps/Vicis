package rs.emulate.editor.legacy.npc

import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.Editors
import org.controlsfx.property.editor.PropertyEditor
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.properties.ResourcePropertySheetItem
import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.legacy.config.npc.NpcDefinitionDecoder

class NpcPropertySupport : ResourcePropertySupport<NpcProperty, LegacyFileId.ConfigEntry> {

    override fun createProperties(
        project: Project<LegacyFileId.ConfigEntry>,
        id: LegacyFileId.ConfigEntry
    ): List<PropertySheet.Item> {
        val loader = project.loader
        val buf = loader.load(id) ?: error("Failed to decode $id.")

        val definition = NpcDefinitionDecoder.decode(id.file, buf)
        return NpcPropertySheetItem.from(definition)
    }

    override fun createEditor(item: ResourcePropertySheetItem<NpcProperty>): PropertyEditor<*>? {
        return when (item.property) { // TODO implement these
            NpcProperty.Models -> null
            NpcProperty.Name -> Editors.createTextEditor(item)
            NpcProperty.Description -> Editors.createTextEditor(item)
            NpcProperty.Size -> Editors.createNumericEditor(item)
            NpcProperty.StandingSequence -> Editors.createNumericEditor(item)
            NpcProperty.WalkingSequence -> Editors.createNumericEditor(item)
            NpcProperty.MovementSequences -> null
            NpcProperty.Actions -> null
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
