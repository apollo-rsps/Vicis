package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.ConfigPropertyType

sealed class FloorProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Colour : FloorProperty<Int>(1)
    object Texture : FloorProperty<Int>(2)
    object UnusedFlag : FloorProperty<Boolean>(3)
    object Occludes : FloorProperty<Boolean>(5)
    object Name : FloorProperty<String>(6)
    object MinimapColour : FloorProperty<Int>(7)
}
