package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.ConfigPropertyType

sealed class BitVariableProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Varbit : BitVariableProperty<rs.emulate.legacy.config.varbit.Variable>(1)
}
