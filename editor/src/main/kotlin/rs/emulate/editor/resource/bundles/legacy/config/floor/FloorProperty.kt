package rs.emulate.editor.resource.bundles.legacy.config.floor

sealed class FloorProperty {
    object Colour : FloorProperty()
    object Texture : FloorProperty()
    object UnusedFlag : FloorProperty()
    object Occludes : FloorProperty()
    object Name : FloorProperty()
    object MinimapColour : FloorProperty()
}
