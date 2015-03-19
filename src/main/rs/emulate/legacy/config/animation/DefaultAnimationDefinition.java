package rs.emulate.legacy.config.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.DefaultDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.Properties;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link AnimationDefinition} used as a base for an actual definitions.
 * 
 * @author Major
 */
public class DefaultAnimationDefinition extends DefaultDefinition {

	/**
	 * The DefaultAnimationDefinition.
	 */
	private static final DefaultAnimationDefinition DEFAULT = new DefaultAnimationDefinition();

	/**
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultAnimationDefinition.
	 */
	private DefaultAnimationDefinition() {
		super();
	}

	@Override
	protected Map<Integer, DefinitionProperty<?>> init() {
		Map<Integer, DefinitionProperty<?>> properties = new HashMap<>(11);

		properties.put(1, new DefinitionProperty<>(AnimationProperty.FRAMES, FrameCollection.EMPTY, FrameCollection::encode,
				FrameCollection::decode, collection -> collection.getSize() * Short.BYTES * 3 + Byte.BYTES));

		properties.put(2, Properties.unsignedShort(AnimationProperty.LOOP_OFFSET, -1));

		Function<DataBuffer, byte[]> interleaveDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			byte[] interleaveOrder = new byte[count];
			
			buffer.get(interleaveOrder);
			return interleaveOrder;
		};

		BiConsumer<DataBuffer, byte[]> interleaveEncoder = (buffer, interleave) -> buffer.putByte(interleave.length).put(interleave);

		properties.put(3, new DefinitionProperty<>(AnimationProperty.INTERLEAVE_ORDER, null, interleaveEncoder,
				interleaveDecoder, interleave -> interleave.length + Byte.BYTES));

		properties.put(4, Properties.alwaysTrue(AnimationProperty.STRETCHES, false));
		properties.put(5, Properties.unsignedByte(AnimationProperty.PRIORITY, 5));
		properties.put(6, Properties.unsignedShort(AnimationProperty.PLAYER_MAINHAND, -1));
		properties.put(7, Properties.unsignedShort(AnimationProperty.PLAYER_OFFHAND, -1));
		properties.put(8, Properties.unsignedByte(AnimationProperty.MAXIMUM_LOOPS, 99));
		properties.put(9, Properties.unsignedByte(AnimationProperty.ANIMATING_PRECEDENCE, -1));
		properties.put(10, Properties.unsignedByte(AnimationProperty.WALKING_PRECEDENCE, -1));
		properties.put(11, Properties.unsignedByte(AnimationProperty.REPLAY_MODE, 2));

		return properties;
	}

}