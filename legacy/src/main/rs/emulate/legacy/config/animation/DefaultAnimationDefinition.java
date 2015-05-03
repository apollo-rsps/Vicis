package rs.emulate.legacy.config.animation;

import static rs.emulate.legacy.config.animation.AnimationProperty.ANIMATING_PRECEDENCE;
import static rs.emulate.legacy.config.animation.AnimationProperty.FRAMES;
import static rs.emulate.legacy.config.animation.AnimationProperty.INTERLEAVE_ORDER;
import static rs.emulate.legacy.config.animation.AnimationProperty.LOOP_OFFSET;
import static rs.emulate.legacy.config.animation.AnimationProperty.MAXIMUM_LOOPS;
import static rs.emulate.legacy.config.animation.AnimationProperty.PLAYER_MAINHAND;
import static rs.emulate.legacy.config.animation.AnimationProperty.PLAYER_OFFHAND;
import static rs.emulate.legacy.config.animation.AnimationProperty.PRIORITY;
import static rs.emulate.legacy.config.animation.AnimationProperty.REPLAY_MODE;
import static rs.emulate.legacy.config.animation.AnimationProperty.STRETCHES;
import static rs.emulate.legacy.config.animation.AnimationProperty.WALKING_PRECEDENCE;
import static rs.emulate.shared.property.Properties.alwaysTrue;
import static rs.emulate.shared.property.Properties.unsignedByte;
import static rs.emulate.shared.property.Properties.unsignedShort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link AnimationDefinition} used as a base for an actual definitions.
 *
 * @author Major
 */
public class DefaultAnimationDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultAnimationDefinition.
	 */
	private static final DefaultAnimationDefinition DEFAULT = new DefaultAnimationDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultAnimationDefinition.
	 */
	private DefaultAnimationDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>(11);

		properties.put(1, new ConfigProperty<>(FRAMES, FrameCollection.EMPTY, FrameCollection::encode,
				FrameCollection::decode, FrameCollection::bytes, input -> Optional.empty())); // XXX

		properties.put(2, unsignedShort(LOOP_OFFSET, -1));

		Function<DataBuffer, byte[]> interleaveDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			byte[] interleaveOrder = new byte[count];

			buffer.get(interleaveOrder);
			return interleaveOrder;
		};

		BiConsumer<DataBuffer, byte[]> interleaveEncoder = (buffer, interleave) -> buffer.putByte(interleave.length)
				.put(interleave);

		properties.put(3, new ConfigProperty<>(INTERLEAVE_ORDER, new byte[0], interleaveEncoder, interleaveDecoder,
				interleave -> interleave.length + Byte.BYTES, input -> Optional.empty())); // XXX

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