package rs.emulate.legacy.config.floor

// TODO blend colours

class FloorDefinition(
    val id: Int,
    var rgb: Int = 0,
    var texture: Int = -1,
    var occludes: Boolean = true,
    var minimapColour: Int = 0
)
