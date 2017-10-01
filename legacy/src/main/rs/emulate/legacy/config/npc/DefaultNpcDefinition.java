package rs.emulate.legacy.config.npc;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.Properties;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.shared.util.DataBuffer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static rs.emulate.legacy.config.Properties.*;
import static rs.emulate.legacy.config.npc.NpcProperty.*;

/**
 * A default {@link NpcDefinition} used as a base.
 *
 * @param <T> The type of NpcDefinition this DefaultNpcDefinition is for.
 * @author Major
 */
public class DefaultNpcDefinition extends DefaultConfigDefinition<NpcDefinition> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> properties = new HashMap<>();

		BiConsumer<DataBuffer, int[]> modelEncoder = (buffer, models) -> {
			buffer.putByte(models.length);
			Arrays.stream(models).forEach(buffer::putShort);
		};

		properties.put(1, new SerializableProperty<>(MODELS, null, modelEncoder, ConfigUtils.MODEL_DECODER,
				models -> models.length * Short.BYTES + Byte.BYTES, input -> Optional.empty())); // FIXME parser

		properties.put(2, asciiString(NAME, "null"));
		properties.put(3, asciiString(DESCRIPTION, "null"));
		properties.put(12, unsignedByte(SIZE, 1));
		properties.put(13, unsignedShort(IDLE_ANIMATION, -1));
		properties.put(14, unsignedShort(WALKING_ANIMATION, -1));

		properties.put(17,
				new SerializableProperty<>(ANIMATION_SET, MovementAnimationSet.EMPTY, MovementAnimationSet::encode,
						MovementAnimationSet::decode, Short.BYTES * 4, input -> Optional.empty())); // FIXME parser

		for (int option = 1; option <= NpcDefinition.INTERACTION_COUNT; option++) {
			ConfigPropertyType name = ConfigUtils.newOptionProperty(NpcDefinition.INTERACTION_PROPERTY_PREFIX, option);
			properties.put(option + 29, asciiString(name, "hidden"));
		}

		properties.put(40, ConfigUtils.newColourProperty(COLOURS));

		properties.put(60, new SerializableProperty<>(SECONDARY_MODELS, null, modelEncoder, ConfigUtils.MODEL_DECODER,
				models -> models.length * Short.BYTES + Byte.BYTES, input -> Optional.empty())); // FIXME parser

		for (int option = 1; option <= 3; option++) {
			properties.put(option + 89, Properties.unsignedShort(ConfigUtils.newOptionProperty("unused", option), 0));
		}

		properties.put(93, alwaysFalse(MINIMAP_VISIBLE, true));
		properties.put(95, unsignedShort(COMBAT_LEVEL, -1));
		properties.put(97, unsignedShort(FLAT_SCALE, ConfigConstants.DEFAULT_SCALE));
		properties.put(98, unsignedShort(HEIGHT_SCALE, ConfigConstants.DEFAULT_SCALE));
		properties.put(99, alwaysTrue(PRIORITY_RENDER, false));

		properties.put(100, unsignedByte(LIGHT_MODIFIER, 0));
		properties.put(101, unsignedByte(SHADOW_MODIFIER, 0)); // TODO !! needs to multiply by 5
		properties.put(102, unsignedShort(HEAD_ICON, -1));
		properties.put(103, unsignedShort(ROTATION, 32));

		properties.put(106, new SerializableProperty<>(MORPHISM_SET, MorphismSet.EMPTY, MorphismSet::encode,
				MorphismSet::decode, morphisms -> Short.BYTES * (2 + morphisms.getCount()) + Byte.BYTES,
				input -> Optional.empty())); // XXX

		properties.put(107, alwaysFalse(CLICKABLE, true));

		return properties;
	}

}