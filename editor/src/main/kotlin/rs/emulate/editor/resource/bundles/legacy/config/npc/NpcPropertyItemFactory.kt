package rs.emulate.editor.resource.bundles.legacy.config.npc

//
//import org.controlsfx.control.PropertySheet
//import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource
//import rs.emulate.editor.ui.workspace.components.propertysheet.MutablePropertyItem
//import rs.emulate.editor.ui.workspace.components.propertysheet.PropertyItemFactory
//import rs.emulate.legacy.config.npc.NpcDefinition
//
//object NpcPropertyItemFactory : PropertyItemFactory<NpcDefinition> {
//
//    override fun generate(resource: ConfigResource<NpcDefinition>): List<PropertySheet.Item> {
//        val definition = resource.definition
//
//        return listOf(
//            MutablePropertyItem(NpcProperty.Name, definition::name),
//            MutablePropertyItem(NpcProperty.Description, definition::description),
//            MutablePropertyItem(NpcProperty.Size, definition::size),
//            MutablePropertyItem(NpcProperty.StandingSequence, definition::standingSequence),
//            MutablePropertyItem(NpcProperty.WalkingSequence, definition::walkingSequence),
//            MutablePropertyItem(NpcProperty.VisibleOnMinimap, definition::visibleOnMinimap),
//            MutablePropertyItem(NpcProperty.CombatLevel, definition::combatLevel),
//            MutablePropertyItem(NpcProperty.PlanarScale, definition::planarScale),
//            MutablePropertyItem(NpcProperty.VerticalScale, definition::verticalScale),
//            MutablePropertyItem(NpcProperty.PriorityRender, definition::priorityRender),
//            MutablePropertyItem(NpcProperty.Brightness, definition::brightness),
//            MutablePropertyItem(NpcProperty.Diffusion, definition::diffusion),
//            MutablePropertyItem(NpcProperty.HeadIcon, definition::headIconId),
//            MutablePropertyItem(NpcProperty.DefaultOrientation, definition::defaultOrientation),
//            MutablePropertyItem(NpcProperty.Clickable, definition::clickable)
//        )
//    }
//
//}
