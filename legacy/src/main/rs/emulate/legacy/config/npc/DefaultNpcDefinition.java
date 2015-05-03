package rs.emulate.legacy.config.npc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.property.Properties;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link NpcDefinition} used as a base.
 *
 * @author Major
 */
public class DefaultNpcDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultNpcDefinition.
	 */
	private static final DefaultNpcDefinition DEFAULT = new DefaultNpcDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultNpcDefinition.
	 */
	private DefaultNpcDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>();

		Function<DataBuffer, int[]> modelDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			int[] models = new int[count];

			Arrays.setAll(models, index -> buffer.getUnsignedShort());
			return models;
		};

		BiConsumer<DataBuffer, int[]> modelEncoder = (buffer, models) -> {
			buffer.putByte(models.length);
			Arrays.stream(models).forEach(buffer::putShort);
		};

		properties.put(1, new ConfigProperty<>(NpcProperty.MODELS, null, modelEncoder, modelDecoder,
				models -> models.length * Short.BYTES + Byte.BYTES, input -> Optional.empty())); // XXX

		properties.put(2, Properties.asciiString(NpcProperty.NAME, "null"));
		properties.put(3, Properties.asciiString(NpcProperty.DESCRIPTION, "null"));
		properties.put(12, Properties.unsignedByte(NpcProperty.SIZE, 1));
		properties.put(13, Properties.unsignedShort(NpcProperty.IDLE_ANIMATION, -1));
		properties.put(14, Properties.unsignedShort(NpcProperty.WALKING_ANIMATION, -1));

		properties
				.put(17,
						new ConfigProperty<>(NpcProperty.ANIMATION_SET, MovementAnimationSet.EMPTY,
								MovementAnimationSet::encode, MovementAnimationSet::decode, Short.BYTES * 4,
								input -> Optional.empty())); // XXX

		for (int option = 1; option <= NpcDefinition.INTERACTION_COUNT; option++) {
			ConfigPropertyType name = ConfigUtils.createOptionProperty(NpcDefinition.INTERACTION_PROPERTY_PREFIX,
					option);
			properties.put(option + 29, Properties.asciiString(name, "hidden"));
		}

		properties.put(40, ConfigUtils.createColourProperty(NpcProperty.COLOURS));

		properties.put(60, new ConfigProperty<>(NpcProperty.SECONDARY_MODELS, null, modelEncoder, modelDecoder,
				models -> models.length * Short.BYTES + Byte.BYTES, input -> Optional.empty())); // XXX

		for (int option = 1; option <= 3; option++) {
			properties
					.put(option + 89, Properties.unsignedShort(ConfigUtils.createOptionProperty("unused", option), 0));
		}

		properties.put(93, Properties.alwaysFalse(NpcProperty.MINIMAP_VISIBLE, true));
		properties.put(95, Properties.unsignedShort(NpcProperty.COMBAT_LEVEL, -1));
		properties.put(97, Properties.unsignedShort(NpcProperty.FLAT_SCALE, ConfigConstants.DEFAULT_SCALE));
		properties.put(98, Properties.unsignedShort(NpcProperty.HEIGHT_SCALE, ConfigConstants.DEFAULT_SCALE));
		properties.put(99, Properties.alwaysTrue(NpcProperty.PRIORITY_RENDER, false));

		properties.put(100, Properties.unsignedByte(NpcProperty.LIGHT_MODIFIER, 0));
		properties.put(101, Properties.unsignedByte(NpcProperty.SHADOW_MODIFIER, 0)); // TODO needs to multiply by 5
		properties.put(102, Properties.unsignedShort(NpcProperty.HEAD_ICON, -1));
		properties.put(103, Properties.unsignedShort(NpcProperty.ROTATION, 32));

		properties.put(106, new ConfigProperty<>(NpcProperty.MORPHISM_SET, MorphismSet.EMPTY, MorphismSet::encode,
				MorphismSet::decode, morphisms -> Short.BYTES * (2 + morphisms.getCount()) + Byte.BYTES,
				input -> Optional.empty())); // XXX

		properties.put(107, Properties.alwaysFalse(NpcProperty.CLICKABLE, true));

		return properties;
	}

}