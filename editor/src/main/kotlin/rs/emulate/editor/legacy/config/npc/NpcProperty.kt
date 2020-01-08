package rs.emulate.editor.legacy.config.npc

import rs.emulate.editor.core.workbench.properties.ResourceProperty

sealed class NpcProperty(
    override val name: String,
    override val category: String,
    override val description: String
) : ResourceProperty {

    object Models : NpcProperty(
        "Models",
        category = "Model",
        description = "The model ids composed to create the npc."
    )

    object Name : NpcProperty(
        "Name",
        category = "Text",
        description = "The name displayed on hover, right click, etc."
    )

    object Description : NpcProperty(
        "Description",
        category = "Text",
        description = "The text shown when examining the npc."
    )

    object Size : NpcProperty(
        "Size",
        category = "Model",
        description = "The size, in tiles, of the npc."
    )

    object StandingSequence : NpcProperty(
        "Standing animation",
        category = "Animations",
        description = "The animation performed when the npc is standing idle."
    )

    object WalkingSequence : NpcProperty(
        "Walking animation",
        category = "Animations",
        description = "The animation performed when the npc is walking."
    )

    object MovementSequences : NpcProperty(
        "Movement animations",
        category = "Animations",
        description = "Set of animations performed when the npc performs different movements."
    )

    object Actions : NpcProperty(
        "Actions",
        category = "Interaction",
        description = "The context menu actions."
    )

    object Colours : NpcProperty(
        "Colours",
        category = "Model",
        description = "The model colours to replace for this npc."
    )

    object WidgetModels : NpcProperty(
        "Widget models",
        category = "Model",
        description = "The model ids composed to display the npc on a widget."
    )

    object VisibleOnMinimap : NpcProperty(
        "Visible on minimap",
        category = "Display",
        description = "Whether or not to display a yellow dot on the client minimap for this npc."
    )

    object CombatLevel : NpcProperty(
        "Combat level",
        category = "Text",
        description = "The combat level displayed on right click (0 displays no level)."
    )

    object PlanarScale : NpcProperty(
        "Planar model scale",
        category = "Display",
        description = "The scale factor to apply with respect to the plane (width/depth)."
    )

    object VerticalScale : NpcProperty(
        "Vertical model scale",
        category = "Display",
        description = "The scale factor to apply vertically."
    )

    object PriorityRender : NpcProperty(
        "High-priority render",
        category = "Display",
        description = "Whether or not this npc should be rendered before other npcs."
    )

    object Brightness : NpcProperty(
        "Brightness",
        category = "Display",
        description = "The brightness offset to apply when lighting this npc's model."
    )

    object Diffusion : NpcProperty(
        "Diffusion",
        category = "Display",
        description = "The diffusion offset to apply when lighting this npc's model."
    )

    object HeadIcon : NpcProperty(
        "Prayer head icon",
        category = "Display",
        description = "The id of the prayer headicon to draw above this npc."
    )

    object DefaultOrientation : NpcProperty(
        "Default orientation",
        category = "Display",
        description = "The default orientation of the npc model."
    )

    object Morphisms : NpcProperty(
        "Morphisms",
        category = "Morphing",
        description = "The npc ids this npc can morph into."
    )

    object Clickable : NpcProperty(
        "Clickable",
        category = "Interaction",
        description = "Whether or not this npc can be clicked by a player."
    )

}
