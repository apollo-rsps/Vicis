package rs.emulate.common.config.kit

import rs.emulate.common.config.Definition

class IdentikitDefinition(
    override val id: Int,
    var part: Int = -1,
    var models: IntArray? = null,
    var playerDesignStyle: Boolean = false,
    var originalColours: IntArray = IntArray(size = 6),
    var replacementColours: IntArray = IntArray(size = 6),
    var widgetModels: IntArray = IntArray(5) { -1 }
) : Definition
