package rs.emulate.legacy.config.object;

import static rs.emulate.legacy.config.object.ObjectProperty.*;
import static rs.emulate.shared.property.Properties.alwaysFalse;
import static rs.emulate.shared.property.Properties.alwaysTrue;
import static rs.emulate.shared.property.Properties.asciiString;
import static rs.emulate.shared.property.Properties.signedByte;
import static rs.emulate.shared.property.Properties.unsignedByte;
import static rs.emulate.shared.property.Properties.unsignedShort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.npc.MorphismSet;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link ObjectDefinition} used as a base for actual definitions.
 *
 * @author Major
 */
public class DefaultObjectDefinition extends DefaultConfigDefinition {

	/**
	 * The default definition.
	 */
	private static final DefaultObjectDefinition DEFAULT = new DefaultObjectDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultObjectDefinition.
	 */
	private DefaultObjectDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>();

		properties.put(1, new ConfigProperty<>(POSITIONED_MODELS, ModelSet.EMPTY, ModelSet::encode,
				ModelSet::decodePositioned, set -> set.getCount() * (Short.BYTES + Byte.BYTES) + Byte.BYTES,
				input -> Optional.empty())); // XXX

		properties.put(2, asciiString(NAME, "null"));
		properties.put(3, asciiString(DESCRIPTION, "null"));

		properties.put(5, new ConfigProperty<>(MODELS, ModelSet.EMPTY, ModelSet::encode, ModelSet::decode,
				set -> Byte.BYTES + set.getCount() * Short.BYTES, input -> Optional.empty())); // XXX

		properties.put(14, unsignedByte(WIDTH, 1));
		properties.put(15, unsignedByte(LENGTH, 1));
		properties.put(17, alwaysFalse(SOLID, true));
		properties.put(18, alwaysFalse(IMPENETRABLE, true));

		properties.put(19, new ConfigProperty<>(INTERACTIVE, false, DataBuffer::putBoolean, DataBuffer::getBoolean,
				Byte.BYTES, input -> Optional.empty())); // XXX

		properties.put(21, alwaysTrue(CONTOUR_GROUND, false));
		properties.put(22, alwaysTrue(DELAY_SHADING, false));
		properties.put(23, alwaysTrue(OCCLUDE, false));
		properties.put(24, unsignedShort(ANIMATION, -1)); // TODO should be -1 if 65_535

		properties.put(28, unsignedByte(DECOR_DISPLACEMENT, 0));
		properties.put(29, signedByte(AMBIENT_LIGHTING, 0));

		for (int option = 1; option <= ObjectDefinition.INTERACTION_COUNT; option++) {
			ConfigPropertyType name = ConfigUtils.newOptionProperty(ObjectDefinition.INTERACTION_PROPERTY_PREFIX,
					option);
			properties.put(option + 29, asciiString(name, "hidden"));
		}

		properties.put(39, signedByte(LIGHT_DIFFUSION, 0));
		properties.put(40, ConfigUtils.newColourProperty(COLOURS));

		properties.put(60, unsignedShort(MINIMAP_FUNCTION, -1));
		properties.put(62, alwaysTrue(INVERTED, false));
		properties.put(64, alwaysFalse(CAST_SHADOW, true));

		properties.put(65, unsignedShort(SCALE_X, ConfigConstants.DEFAULT_SCALE));
		properties.put(66, unsignedShort(SCALE_Y, ConfigConstants.DEFAULT_SCALE));
		properties.put(67, unsignedShort(SCALE_Z, ConfigConstants.DEFAULT_SCALE));

		properties.put(68, unsignedShort(MAPSCENE, -1));
		properties.put(69, unsignedByte(SURROUNDINGS, 0));

		properties.put(70, unsignedShort(TRANSLATION_X, 0));
		properties.put(71, unsignedShort(TRANSLATION_Y, 0));
		properties.put(72, unsignedShort(TRANSLATION_Z, 0));

		properties.put(73, alwaysTrue(OBSTRUCTIVE_GROUND, false));
		properties.put(74, alwaysTrue(HOLLOW, false));
		properties.put(75, unsignedByte(SUPPORTS_ITEMS, 0));

		properties.put(77, new ConfigProperty<>(MORPHISM_SET, MorphismSet.EMPTY, MorphismSet::encode,
				MorphismSet::decode, morphisms -> Short.BYTES * (2 + morphisms.getCount()) + Byte.BYTES,
				input -> Optional.empty())); // XXX

		return properties;
	}

}