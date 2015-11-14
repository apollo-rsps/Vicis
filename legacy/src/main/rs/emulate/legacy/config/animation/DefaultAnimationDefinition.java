package rs.emulate.legacy.config.animation;

import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.shared.util.DataBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static rs.emulate.legacy.config.Properties.alwaysTrue;
import static rs.emulate.legacy.config.Properties.unsignedByte;
import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.animation.AnimationProperty.*;

/**
 * A default {@link AnimationDefinition} used as a base for an actual definitions.
 *
 * @param <T> The type of {@link AnimationDefinition} this default is for.
 * @author Major
 */
public class DefaultAnimationDefinition<T extends AnimationDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> properties = new HashMap<>(11);

		properties.put(1, new SerializableProperty<>(FRAMES, FrameCollection.EMPTY, FrameCollection::encode,
				FrameCollection::decode, FrameCollection::bytes, input -> Optional.empty())); // FIXME parser

		properties.put(2, unsignedShort(LOOP_OFFSET, -1));

		Function<DataBuffer, byte[]> interleaveDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			byte[] interleaveOrder = new byte[count];

			buffer.get(interleaveOrder);
			return interleaveOrder;
		};

		BiConsumer<DataBuffer, byte[]> interleaveEncoder = (buffer, order) -> buffer.putByte(order.length).put(order);

		properties.put(3, new SerializableProperty<>(INTERLEAVE_ORDER, new byte[0], interleaveEncoder, interleaveDecoder,
				interleave -> interleave.length + Byte.BYTES, input -> Optional.empty())); // FIXME parser

		properties.put(4, alwaysTrue(STRETCHES, false));
		properties.put(5, unsignedByte(PRIORITY, 5));
		properties.put(6, unsignedShort(PLAYER_MAINHAND, -1));
		properties.put(7, unsignedShort(PLAYER_OFFHAND, -1));
		properties.put(8, unsignedByte(MAXIMUM_LOOPS, 99));
		properties.put(9, unsignedByte(ANIMATING_PRECEDENCE, -1));
		properties.put(10, unsignedByte(WALKING_PRECEDENCE, -1));
		properties.put(11, unsignedByte(REPLAY_MODE, 2));

		return properties;
	}

}