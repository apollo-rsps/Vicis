package rs.emulate.legacy.config.npc

import io.netty.buffer.ByteBuf

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
         * Decodes a MovementAnimationSet from the specified [ByteBuf].
         */
        fun decode(buffer: ByteBuf): MovementAnimationSet {
            val walking = buffer.readUnsignedShort()
            val halfTurn = buffer.readUnsignedShort()
            val clockwiseQuarterTurn = buffer.readUnsignedShort()
            val anticlockwiseQuarterTurn = buffer.readUnsignedShort()

            return MovementAnimationSet(walking, halfTurn, clockwiseQuarterTurn, anticlockwiseQuarterTurn)
        }

        /**
         * Encodes the specified [MovementAnimationSet] into the specified [ByteBuf].
         */
        fun encode(buffer: ByteBuf, set: MovementAnimationSet): ByteBuf {
            return buffer.writeShort(set.walking)
                .writeShort(set.halfTurn)
                .writeShort(set.clockwiseQuarterTurn)
                .writeShort(set.anticlockwiseQuarterTurn)
        }
    }

}
