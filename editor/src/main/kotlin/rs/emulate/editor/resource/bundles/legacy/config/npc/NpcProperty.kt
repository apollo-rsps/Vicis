package rs.emulate.editor.resource.bundles.legacy.config.npc

import rs.emulate.editor.ui.workspace.components.propertysheet.ListedProperty

sealed class NpcProperty(
    override val name: String,
    override val category: String,
    override val description: String
) : ListedProperty {

    object Models : NpcProperty("Models",
        category = "model",
        description = "The model ids composed to create the npc."
    )

    object Name : NpcProperty("Name",
        category = "text",
        description = "The name displayed on hover, right click, etc."
    )

    object Description : NpcProperty("Description",
        category = "text",
        description = "The text shown when examining the npc."
    )

    object Size : NpcProperty("Size",
        category = "model",
        description = "The size, in tiles, of the npc."
    )

    object StandingSequence : NpcProperty("Standing animation",
        category = "animations",
        description = "The animation performed when the npc is standing idle."
    )

    object WalkingSequence : NpcProperty("Walking animation",
        category = "animations",
        description = "The animation performed when the npc is walking."
    )

    object MovementSequences : NpcProperty("Movement animations",
        category = "animations",
        description = "Set of animations performed when the npc performs different movements."
    )

    object Action1 : NpcProperty("Action1",
        category = "interaction",
        description = "The primary action, displayed first on the context menu."
    )

    object Action2 : NpcProperty("Action2",
        category = "interaction",
        description = "The action displayed second on the context menu."
    )

    object Action3 : NpcProperty("Action3",
        category = "interaction",
        description = "The action displayed third on the context menu."
    )

    object Action4 : NpcProperty("Action4",
        category = "interaction",
        description = "The action displayed fourth on the context menu."
    )

    object Action5 : NpcProperty("Action5",
        category = "interaction",
        description = "The action displayed fifth on the context menu."
    )

    object Colours : NpcProperty("Colours",
        category = "model",
        description = "The model colours to replace for this npc."
    )

    object WidgetModels : NpcProperty("Widget models",
        category = "model",
        description = "The model ids composed to display the npc on a widget."
    )

    object VisibleOnMinimap : NpcProperty("Visible on minimap",
        category = "display",
        description = "Whether or not to display a yellow dot on the client minimap for this npc."
    )

    object CombatLevel : NpcProperty("Combat level",
        category = "text",
        description = "The combat level displayed on right click (0 displays no level)."
    )

    object PlanarScale : NpcProperty("Planar model scale",
        category = "display",
        description = "The scale factor to apply with respect to the plane (width/depth)."
    )

    object VerticalScale : NpcProperty("Vertical model scale",
        category = "display",
        description = "The scale factor to apply vertically."
    )

    object PriorityRender : NpcProperty("High-priority render",
        category = "display",
        description = "Whether or not this npc should be rendered before other npcs."
    )

    object Brightness : NpcProperty("Brightness",
        category = "display",
        description = "The brightness offset to apply when lighting this npc's model."
    )

    object Diffusion : NpcProperty("Diffusion",
        category = "display",
        description = "The diffusion offset to apply when lighting this npc's model."
    )

    object HeadIcon : NpcProperty("Prayer head icon",
        category = "display",
        description = "The id of the prayer headicon to draw above this npc."
    )

    object DefaultOrientation : NpcProperty("Default orientation",
        category = "display",
        description = "The default orientation of the npc model."
    )

    object Morphisms : NpcProperty("Morphisms",
        category = "morphisms",
        description = "The npc ids this npc can morph into."
    )

    object Clickable : NpcProperty("Clickable",
        category = "interaction",
        description = "Whether or not this npc can be clicked by a player."
    )

}
