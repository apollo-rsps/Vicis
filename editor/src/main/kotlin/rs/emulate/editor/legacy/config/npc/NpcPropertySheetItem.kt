package rs.emulate.editor.legacy.config.npc

import org.controlsfx.property.editor.PropertyEditor
import rs.emulate.editor.core.workbench.properties.ObservableProperty
import rs.emulate.editor.core.workbench.properties.ResourcePropertySheetItem
import rs.emulate.editor.core.workbench.properties.editors.CheckEditor
import rs.emulate.editor.core.workbench.properties.editors.IntEditor
import rs.emulate.editor.core.workbench.properties.editors.TextEditor
import rs.emulate.editor.vfs.LegacyFileLoader
import rs.emulate.legacy.config.npc.NpcDefinition
import kotlin.reflect.KClass

sealed class NpcPropertySheetItem(
    observable: ObservableProperty<out Any?>,
    propertyEditorClass: KClass<out PropertyEditor<*>>
) : ResourcePropertySheetItem<NpcProperty>(observable, propertyEditorClass) {

    class Name(initialValue: String) : NpcPropertySheetItem(
        ObservableNpcProperty.Name(initialValue),
        TextEditor::class
    )

    class Description(initialValue: String?) : NpcPropertySheetItem(
        ObservableNpcProperty.Description(initialValue),
        TextEditor::class
    )

    class Size(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.Size(initialValue),
        IntEditor::class
    )

    class StandingSequence(initialValue: Int, loader: LegacyFileLoader) : NpcPropertySheetItem(
        ObservableNpcProperty.StandingSequence(initialValue, loader),
        IntEditor::class
    )

    class WalkingSequence(initialValue: Int, loader: LegacyFileLoader) : NpcPropertySheetItem(
        ObservableNpcProperty.WalkingSequence(initialValue, loader),
        IntEditor::class
    )

    class VisibleOnMinimap(initialValue: Boolean) : NpcPropertySheetItem(
        ObservableNpcProperty.VisibleOnMinimap(initialValue),
        CheckEditor::class
    )

    class CombatLevel(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.CombatLevel(initialValue),
        IntEditor::class
    )

    class PlanarScale(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.PlanarScale(initialValue),
        IntEditor::class
    )

    class VerticalScale(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.VerticalScale(initialValue),
        IntEditor::class
    )

    class PriorityRender(initialValue: Boolean) : NpcPropertySheetItem(
        ObservableNpcProperty.PriorityRender(initialValue),
        CheckEditor::class
    )

    class Brightness(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.Brightness(initialValue),
        IntEditor::class
    )

    class Diffusion(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.Diffusion(initialValue),
        IntEditor::class
    )

    class HeadIcon(initialValue: Int, loader: LegacyFileLoader) : NpcPropertySheetItem(
        ObservableNpcProperty.HeadIcon(initialValue, loader),
        IntEditor::class
    )

    class DefaultOrientation(initialValue: Int) : NpcPropertySheetItem(
        ObservableNpcProperty.DefaultOrientation(initialValue),
        IntEditor::class
    )

    class Clickable(initialValue: Boolean) : NpcPropertySheetItem(
        ObservableNpcProperty.Clickable(initialValue),
        CheckEditor::class
    )

    companion object {

        fun from(definition: NpcDefinition, loader: LegacyFileLoader): List<NpcPropertySheetItem> {
            return listOf(
                Name(definition.name),
                Description(definition.description),
                Size(definition.size),
                StandingSequence(definition.standingSequence, loader),
                WalkingSequence(definition.walkingSequence, loader),
                VisibleOnMinimap(definition.visibleOnMinimap),
                CombatLevel(definition.combatLevel),
                PlanarScale(definition.planarScale),
                VerticalScale(definition.verticalScale),
                PriorityRender(definition.priorityRender),
                Brightness(definition.brightness),
                Diffusion(definition.diffusion),
                HeadIcon(definition.headIconId, loader),
                DefaultOrientation(definition.defaultOrientation),
                Clickable(definition.clickable)
            )
        }

    }

}

