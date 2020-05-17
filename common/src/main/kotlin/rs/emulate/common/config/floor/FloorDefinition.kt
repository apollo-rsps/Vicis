package rs.emulate.common.config.floor

import rs.emulate.common.config.Definition

// TODO blend colours

class FloorDefinition(
    override val id: Int,
    var rgb: Int = 0,
    var texture: Int = -1,
    var occludes: Boolean = true,
    var minimapColour: Int = 0
) : Definition
