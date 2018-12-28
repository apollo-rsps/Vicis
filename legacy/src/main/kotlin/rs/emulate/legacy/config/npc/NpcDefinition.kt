package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.Definition

class NpcDefinition(
    override val id: Int,
    var models: MutableList<Int> = mutableListOf(),
    var name: String = "null",
    var description: String? = null,
    var size: Int = 1,
    var standingSequence: Int = -1,
    var walkingSequence: Int = -1,
    var movementSequences: MovementAnimationSet = MovementAnimationSet.EMPTY,
    var actions: Array<String?> = arrayOfNulls(10),
    var replacementColours: MutableMap<Int, Int> = mutableMapOf(),
    var widgetModels: IntArray? = null,
    var visibleOnMinimap: Boolean = true,
    var combatLevel: Int = -1,
    var planarScale: Int = 128,
    var verticalScale: Int = 128,
    var priorityRender: Boolean = false,
    var brightness: Int = 0,
    var diffusion: Int = 0,
    var headIconId: Int = -1,
    var defaultOrientation: Int = 32,
    var morphisms: MorphismSet = MorphismSet.EMPTY,
    var clickable: Boolean = true
) : Definition

