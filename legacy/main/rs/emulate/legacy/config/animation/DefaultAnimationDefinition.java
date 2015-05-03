package rs.emulate.legacy.config.animation;

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

		properties.put(1, new ConfigProperty<>(AnimationProperty.FRAMES, FrameCollection.EMPTY, FrameCollection::encode,
 FrameCollection::decode, FrameCollection::bytes, input -> Optional.empty())); // XXX

		properties.put(2, unsignedShort(AnimationProperty.LOOP_OFFSET, -1));

		Function<DataBuffer, byte[]> interleaveDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			byte[] interleaveOrder = new byte[count];

			buffer.get(interleaveOrder);
			return interleaveOrder;
		};

		BiConsumer<DataBuffer, byte[]> interleaveEncoder = (buffer, interleave) -> buffer.putByte(interleave.length).put(
				interleave);

		properties.put(3, new ConfigProperty<>(AnimationProperty.INTERLEAVE_ORDER, new byte[0], interleaveEncoder,
				interleaveDecoder, interleave -> interleave.length + Byte.BYTES, input -> Optional.empty())); // XXX

		properties.put(4, alwaysTrue(AnimationProperty.STRETCHES, false));
		properties.put(5, unsignedByte(AnimationProperty.PRIORITY, 5));
		properties.put(6, unsignedShort(AnimationProperty.PLAYER_MAINHAND, -1));
		properties.put(7, unsignedShort(AnimationProperty.PLAYER_OFFHAND, -1));
		properties.put(8, unsignedByte(AnimationProperty.MAXIMUM_LOOPS, 99));
		properties.put(9, unsignedByte(AnimationProperty.ANIMATING_PRECEDENCE, -1));
		properties.put(10, unsignedByte(AnimationProperty.WALKING_PRECEDENCE, -1));
		properties.put(11, unsignedByte(AnimationProperty.REPLAY_MODE, 2));

		return properties;
	}

}