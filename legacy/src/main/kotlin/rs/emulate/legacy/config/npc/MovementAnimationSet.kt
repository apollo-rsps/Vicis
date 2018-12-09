package rs.emulate.legacy.config.npc

import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

/**
 * A wrapper class containing four movement animations used by an npc.
 *
 * @param walking The walking animation id.
 * @param halfTurn The half turn animation id.
 * @param clockwiseQuarterTurn The clockwise quarter turn animation id.
 * @param anticlockwiseQuarterTurn The anticlockwise quarter turn animation id.
 */
data class MovementAnimationSet(
    val walking: Int,
    val halfTurn: Int,
    val clockwiseQuarterTurn: Int,
    val anticlockwiseQuarterTurn: Int
) {

    init {
        require(walking >= -1 && halfTurn >= -1 && clockwiseQuarterTurn >= -1 && anticlockwiseQuarterTurn >= -1) {
            "Specified animation ids must be greater than or equal to -1."
        }
    }

    companion object {

        /**
         * The empty MovementAnimationSet instance, to be used as the default value.
         */
        val EMPTY = MovementAnimationSet(-1, -1, -1, -1)

        /**
         * Decodes a MovementAnimationSet from the specified [ByteBuffer].
         */
        fun decode(buffer: ByteBuffer): MovementAnimationSet {
            val walking = buffer.getUnsignedShort()
            val halfTurn = buffer.getUnsignedShort()
            val clockwiseQuarterTurn = buffer.getUnsignedShort()
            val anticlockwiseQuarterTurn = buffer.getUnsignedShort()

            return MovementAnimationSet(walking, halfTurn, clockwiseQuarterTurn,
                anticlockwiseQuarterTurn)
        }

        /**
         * Encodes the specified [MovementAnimationSet] into the specified [ByteBuffer].
         */
        fun encode(buffer: ByteBuffer, set: MovementAnimationSet): ByteBuffer {
            return buffer.putShort(set.walking.toShort())
                .putShort(set.halfTurn.toShort())
                .putShort(set.clockwiseQuarterTurn.toShort())
                .putShort(set.anticlockwiseQuarterTurn.toShort())
        }
    }

}
