package rs.emulate.editor.resource.bundles.legacy.config.spotanim

sealed class SpotAnimationProperty {
    object Model : SpotAnimationProperty()
    object Sequence : SpotAnimationProperty()
    object PlanarScale : SpotAnimationProperty()
    object VerticalScale : SpotAnimationProperty()
    object Orientation : SpotAnimationProperty()
    object Brightness : SpotAnimationProperty()
    object Diffusion : SpotAnimationProperty()
    object Colours : SpotAnimationProperty()
}
