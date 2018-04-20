package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.alwaysTrue
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedShort
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.animation.AnimationProperty.*
import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * A default [AnimationDefinition] used as a base for an actual definitions.
 *
 * @param T The type of [AnimationDefinition] this default is for.
 */
class DefaultAnimationDefinition<T : AnimationDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val properties = HashMap<Int, SerializableProperty<*>>(11)

        properties[1] = SerializableProperty(FRAMES, FrameCollection.EMPTY, FrameCollection.Companion::encode,
            FrameCollection.Companion::decode, FrameCollection.Companion::bytes
        )

        properties[2] = unsignedShort(LOOP_OFFSET, -1)

        val interleaveDecoder = { buffer: DataBuffer ->
            val count = buffer.getUnsignedByte()
            val interleaveOrder = ByteArray(count)

            buffer.read(interleaveOrder)
            interleaveOrder
        }

        val interleaveEncoder: (DataBuffer, ByteArray) -> DataBuffer = { buffer, order ->
            buffer.putByte(order.size).put(order)
        }

        properties[3] = SerializableProperty(INTERLEAVE_ORDER, ByteArray(0), interleaveEncoder,
            interleaveDecoder, { interleave -> interleave.size + java.lang.Byte.BYTES }
        )

        properties[4] = alwaysTrue(STRETCHES, false)
        properties[5] = unsignedByte(PRIORITY, 5)
        properties[6] = unsignedShort(PLAYER_MAINHAND, -1)
        properties[7] = unsignedShort(PLAYER_OFFHAND, -1)
        properties[8] = unsignedByte(MAXIMUM_LOOPS, 99)
        properties[9] = unsignedByte(ANIMATING_PRECEDENCE, -1)
        properties[10] = unsignedByte(WALKING_PRECEDENCE, -1)
        properties[11] = unsignedByte(REPLAY_MODE, 2)

        return properties
    }

}
