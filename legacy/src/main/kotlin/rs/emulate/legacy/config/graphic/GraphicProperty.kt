package rs.emulate.legacy.config.graphic

import rs.emulate.legacy.config.ConfigPropertyType

sealed class GraphicProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Model : GraphicProperty<Int>(1)
    object Animation : GraphicProperty<Int>(2)
    object BreadthScale : GraphicProperty<Int>(4)
    object DepthScale : GraphicProperty<Int>(5)
    object Rotation : GraphicProperty<Int>(6)
    object Brightness : GraphicProperty<Int>(7)
    object Shadow : GraphicProperty<Int>(8)
} // TODO also has colour properties
