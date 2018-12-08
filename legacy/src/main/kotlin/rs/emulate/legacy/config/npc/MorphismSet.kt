package rs.emulate.legacy.config.npc

import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedShort
import rs.emulate.shared.util.putByte
import java.nio.ByteBuffer
import java.util.Arrays

/**
 * A set of morphisms for an npc or object.
 *
 * @param varbit The morphism variable bit.
 * @param varp The morphism variable parameter.
 * @param morphisms The morphisms.
 */
data class MorphismSet(
    val bitVariable: Int,
    val parameterVariable: Int,
    val morphisms: IntArray
) {

    /**
     * Gets the amount of morphisms.
     */
    val count: Int
        get() = morphisms.size

    companion object {

        /**
         * The empty morphisms, used as a default value.
         */
        val EMPTY = MorphismSet(-1, -1, IntArray(0))

        /**
         * Decodes a [MorphismSet] from the specified [ByteBuffer].
         */
        fun decode(buffer: ByteBuffer): MorphismSet {
            val varbit = buffer.getUnsignedShort()
            val varp = buffer.getUnsignedShort()
            val count = buffer.getUnsignedByte()
            val morphisms = IntArray(count + 1) { buffer.getUnsignedShort() }

            return MorphismSet(varbit, varp, morphisms)
        }

        /**
         * Encodes the specified [MorphismSet] into the specified [ByteBuffer].
         */
        fun encode(buffer: ByteBuffer, set: MorphismSet): ByteBuffer {
            buffer.putShort(set.bitVariable.toShort())
                .putShort(set.parameterVariable.toShort())
                .putByte(set.count - 1)
            set.morphisms.forEach { buffer.putShort(it.toShort()) }

            return buffer
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MorphismSet

        if (bitVariable != other.bitVariable) return false
        if (parameterVariable != other.parameterVariable) return false
        if (!Arrays.equals(morphisms, other.morphisms)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitVariable
        result = 31 * result + parameterVariable
        result = 31 * result + Arrays.hashCode(morphisms)
        return result
    }

}
