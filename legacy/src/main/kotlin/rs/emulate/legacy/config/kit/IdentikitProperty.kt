package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.ConfigPropertyType

sealed class IdentikitProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Part : IdentikitProperty<rs.emulate.legacy.config.kit.Part>(1)
    object Models : IdentikitProperty<IntArray>(2)
    object PlayerDesignStyle : IdentikitProperty<Boolean>(3)
}
