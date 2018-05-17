package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.varbit.BitVariableProperty.Varbit
import rs.emulate.shared.util.DataBuffer

/**
 * A default [BitVariableDefinition] used as a base for an actual definition.
 *
 * @param <T> The type of [BitVariableDefinition] this default is for.
 */
class DefaultBitVariableDefinition<T : BitVariableDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val encoder = { buffer: DataBuffer, bits: Variable ->
            buffer.putShort(bits.variable)
                .putByte(bits.high)
                .putByte(bits.low)
        }

        val decoder = { buffer: DataBuffer ->
            val variable = buffer.getUnsignedShort()
            val low = buffer.getUnsignedByte()
            val high = buffer.getUnsignedByte()

            Variable.create(variable, low, high)
        }

        return hashMapOf(
            1 to SerializableProperty(Varbit, Variable.EMPTY, encoder, decoder,
                java.lang.Short.BYTES + 2 * java.lang.Byte.BYTES
            )
        )
    }

}
