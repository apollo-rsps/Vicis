package rs.emulate.common.config.location

import rs.emulate.common.config.Definition
import rs.emulate.common.config.npc.MorphismSet

class LocationDefinition(
    override val id: Int,
    var models: IntArray = IntArray(0),
    var modelTypes: IntArray = IntArray(0),
    var name: String = "null",
    var description: String = "",
    var width: Int = 0,
    var length: Int = 0,
    var solid: Boolean = true,
    var impenetrable: Boolean = true,
    var interactive: Int = -1,
    var contourGround: Boolean = false,
    var delayShading: Boolean = false,
    var occludes: Boolean = false,
    var sequenceId: Int = 0,
    var decorDisplacement: Int = 0,
    var brightness: Int = 0,
    var diffusion: Int = 0,
    var actions: Array<String?> = arrayOfNulls(10),
    var colours: MutableMap<Int, Int> = mutableMapOf(),
    var minimapFunction: Int = 0,
    var inverted: Boolean = false,
    var castShadow: Boolean = true,
    var scaleX: Int = 0,
    var scaleY: Int = 0,
    var scaleZ: Int = 0,
    var mapsceneId: Int = 0,
    var surroundings: Int = 0,
    var translateX: Int = 0,
    var translateY: Int = 0,
    var translateZ: Int = 0,
    var obstructsGround: Boolean = false,
    var hollow: Boolean = false,
    var supportsItems: Int = if (solid) 1 else -1,
    var morphisms: MorphismSet = MorphismSet.EMPTY
) : Definition
