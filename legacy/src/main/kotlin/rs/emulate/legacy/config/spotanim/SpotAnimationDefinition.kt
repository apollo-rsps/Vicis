package rs.emulate.legacy.config.spotanim

class SpotAnimationDefinition(
    val id: Int,
    var model: Int = 0,
    var sequenceId: Int = -1,
    var planarScale: Int = 128,
    var verticalScale: Int = 128,
    var orientation: Int = 0,
    var modelBrightness: Int = 0,
    var modelDiffusion: Int = 0,
    var originalColours: IntArray = IntArray(size = 10),
    var replacementColours: IntArray = IntArray(size = 10)
)
