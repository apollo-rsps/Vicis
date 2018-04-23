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

        properties[1] = SerializableProperty(Frames, FrameCollection.EMPTY, FrameCollection.Companion::encode,
            FrameCollection.Companion::decode, FrameCollection.Companion::bytes
        )

        properties[2] = unsignedShort(LoopOffset, -1)

        val interleaveDecoder = { buffer: DataBuffer ->
            val count = buffer.getUnsignedByte()
            val interleaveOrder = ByteArray(count)

            buffer.read(interleaveOrder)
            interleaveOrder
        }

        val interleaveEncoder: (DataBuffer, ByteArray) -> DataBuffer = { buffer, order ->
            buffer.putByte(order.size).put(order)
        }

        properties[3] = SerializableProperty(InterleaveOrder, ByteArray(0), interleaveEncoder,
            interleaveDecoder, { interleave -> interleave.size + java.lang.Byte.BYTES }
        )

        properties[4] = alwaysTrue(Stretches, false)
        properties[5] = unsignedByte(Priority, 5)
        properties[6] = unsignedShort(CharacterMainhand, -1)
        properties[7] = unsignedShort(CharacterOffhand, -1)
        properties[8] = unsignedByte(MaximumLoops, 99)
        properties[9] = unsignedByte(AnimatingPrecedence, -1)
        properties[10] = unsignedByte(WalkingPrecedence, -1)
        properties[11] = unsignedByte(ReplayMode, 2)

        return properties
    }

}
