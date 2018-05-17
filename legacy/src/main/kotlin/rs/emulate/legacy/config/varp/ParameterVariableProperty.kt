package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.ConfigPropertyType

sealed class ParameterVariableProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Parameter : ParameterVariableProperty<Int>(1)
}
