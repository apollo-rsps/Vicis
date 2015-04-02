package rs.emulate.legacy.config.object;

import static rs.emulate.shared.prop.Properties.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.npc.MorphismSet;
import rs.emulate.shared.prop.PropertyMap;
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
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the default object definition.
	 */
	private DefaultObjectDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>();

		properties.put(1, new ConfigProperty<>(ObjectProperty.POSITIONED_MODELS, ModelSet.EMPTY, ModelSet::encode,
				ModelSet::decodePositioned, set -> set.getCount() * (Short.BYTES + Byte.BYTES) + Byte.BYTES));

		properties.put(2, string(ObjectProperty.NAME, "null"));
		properties.put(3, string(ObjectProperty.DESCRIPTION, "null"));

		properties.put(5, new ConfigProperty<>(ObjectProperty.MODELS, ModelSet.EMPTY, ModelSet::encode, ModelSet::decode,
				set -> Byte.BYTES + set.getCount() * Short.BYTES));

		properties.put(14, unsignedByte(ObjectProperty.WIDTH, 1));
		properties.put(15, unsignedByte(ObjectProperty.LENGTH, 1));
		properties.put(17, alwaysFalse(ObjectProperty.SOLID, true));
		properties.put(18, alwaysFalse(ObjectProperty.IMPENETRABLE, true));

		properties.put(19, new ConfigProperty<>(ObjectProperty.INTERACTIVE, false, DataBuffer::putBoolean,
				DataBuffer::getBoolean, Byte.BYTES));

		properties.put(21, alwaysTrue(ObjectProperty.CONTOUR_GROUND, false));
		properties.put(22, alwaysTrue(ObjectProperty.DELAY_SHADING, false));
		properties.put(23, alwaysTrue(ObjectProperty.OCCLUDE, false));
		properties.put(24, unsignedShort(ObjectProperty.ANIMATION, -1)); // TODO should be -1 if 65_535

		properties.put(28, unsignedByte(ObjectProperty.DECOR_DISPLACEMENT, 0));
		properties.put(29, signedByte(ObjectProperty.AMBIENT_LIGHTING, 0));

		for (int option = 1; option <= ObjectDefinition.INTERACTION_COUNT; option++) {
			ConfigPropertyType name = ConfigDefinitionUtils.createOptionProperty(ObjectDefinition.INTERACTION_PROPERTY_PREFIX,
					option);
			properties.put(option + 29, string(name, "hidden"));
		}

		properties.put(39, signedByte(ObjectProperty.LIGHT_DIFFUSION, 0));
		properties.put(40, ConfigDefinitionUtils.createColourProperty(ObjectProperty.COLOURS));

		properties.put(60, unsignedShort(ObjectProperty.MINIMAP_FUNCTION, -1));
		properties.put(62, alwaysTrue(ObjectProperty.INVERTED, false));
		properties.put(64, alwaysFalse(ObjectProperty.CAST_SHADOW, true));

		properties.put(65, unsignedShort(ObjectProperty.SCALE_X, ConfigConstants.DEFAULT_SCALE));
		properties.put(66, unsignedShort(ObjectProperty.SCALE_Y, ConfigConstants.DEFAULT_SCALE));
		properties.put(67, unsignedShort(ObjectProperty.SCALE_Z, ConfigConstants.DEFAULT_SCALE));

		properties.put(68, unsignedShort(ObjectProperty.MAPSCENE, -1));
		properties.put(69, unsignedByte(ObjectProperty.SURROUNDINGS, 0));

		properties.put(70, unsignedShort(ObjectProperty.TRANSLATION_X, 0));
		properties.put(71, unsignedShort(ObjectProperty.TRANSLATION_Y, 0));
		properties.put(72, unsignedShort(ObjectProperty.TRANSLATION_Z, 0));

		properties.put(73, alwaysTrue(ObjectProperty.OBSTRUCTIVE_GROUND, false));
		properties.put(74, alwaysTrue(ObjectProperty.HOLLOW, false));
		properties.put(75, unsignedByte(ObjectProperty.SUPPORTS_ITEMS, 0));

		properties.put(77, new ConfigProperty<>(ObjectProperty.MORPHISM_SET, MorphismSet.EMPTY, MorphismSet::encode,
				MorphismSet::decode, morphisms -> Short.BYTES * (2 + morphisms.getCount()) + Byte.BYTES));

		return properties;
	}

}