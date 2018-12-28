package rs.emulate.editor.legacy.npc

import rs.emulate.editor.vfs.data.VirtualFileData
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.model.Model

class NpcData(def: NpcDefinition, models: List<Model>) : VirtualFileData() {
    val name = property(def::name)
    val description = property(def::description)
    val clickable = boolProperty(def::clickable)
    val size = intProperty(def::size)
    val visibleOnMiniMap = boolProperty(def::visibleOnMinimap)
    val combatLevel = intProperty(def::combatLevel)

    val standingSequence = intProperty(def::standingSequence)
    val walkingSequence = intProperty(def::walkingSequence)
    val movementSequences = property(def::movementSequences)

    val planarScale = intProperty(def::planarScale)
    val verticalScale = intProperty(def::verticalScale)
    val priorityRender = boolProperty(def::priorityRender)

//    var models = mappedListProperty(models, Model::id, def::models)

//    var actions: Array<String?> = arrayOfNulls(10),
//    var replacementColours: MutableMap<Int, Int> = mutableMapOf(),
//    var widgetModels: IntArray? = null,
//
//    var brightness: Int = 0,
//    var diffusion: Int = 0,
//    var headIconId: Int = -1,
//    var defaultOrientation: Int = 32,
//    var morphisms: MorphismSet = MorphismSet.EMPTY,
}
