package rs.emulate.legacy.config.object;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.npc.MorphismSet;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.Properties;
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
	protected Map<Integer, DefinitionProperty<?>> init() {
		Map<Integer, DefinitionProperty<?>> properties = new HashMap<>();

		properties.put(1, new DefinitionProperty<>(ObjectProperty.POSITIONED_MODELS, ModelSet.EMPTY, ModelSet::encode,
				ModelSet::decodePositioned, set -> set.getCount() * (Short.BYTES + Byte.BYTES) + Byte.BYTES));

		properties.put(2, Properties.string(ObjectProperty.NAME, "null"));
		properties.put(3, Properties.string(ObjectProperty.DESCRIPTION, "null"));

		properties.put(5, new DefinitionProperty<>(ObjectProperty.MODELS, ModelSet.EMPTY, ModelSet::encode, ModelSet::decode,
				set -> Byte.BYTES + set.getCount() * Short.BYTES));

		properties.put(14, Properties.unsignedByte(ObjectProperty.WIDTH, 1));
		properties.put(15, Properties.unsignedByte(ObjectProperty.LENGTH, 1));
		properties.put(17, Properties.alwaysFalse(ObjectProperty.SOLID, true));
		properties.put(18, Properties.alwaysFalse(ObjectProperty.IMPENETRABLE, true));

		properties.put(19, new DefinitionProperty<>(ObjectProperty.INTERACTIVE, false, DataBuffer::putBoolean, DataBuffer::getBoolean,
				Byte.BYTES));

		properties.put(21, Properties.alwaysTrue(ObjectProperty.CONTOUR_GROUND, false));
		properties.put(22, Properties.alwaysTrue(ObjectProperty.DELAY_SHADING, false));
		properties.put(23, Properties.alwaysTrue(ObjectProperty.OCCLUDE, false));
		properties.put(24, Properties.unsignedShort(ObjectProperty.ANIMATION, -1)); // TODO should be -1 if 65_535

		properties.put(28, Properties.unsignedByte(ObjectProperty.DECOR_DISPLACEMENT, 0));
		properties.put(29, Properties.unsignedByte(ObjectProperty.AMBIENT_LIGHTING, 0));

		for (int option = 1; option <= ObjectDefinition.INTERACTION_COUNT; option++) {
			ConfigPropertyType name = ConfigDefinitionUtils.createOptionProperty(ObjectDefinition.INTERACTION_PROPERTY_PREFIX, option);
			properties.put(option + 29, Properties.string(name, "hidden"));
		}

		properties.put(39, Properties.unsignedByte(ObjectProperty.LIGHT_DIFFUSION, 0));
		properties.put(40, ConfigDefinitionUtils.createColourProperty(ObjectProperty.COLOURS));

		properties.put(60, Properties.unsignedShort(ObjectProperty.MINIMAP_FUNCTION, -1));
		properties.put(62, Properties.alwaysTrue(ObjectProperty.INVERTED, false));
		properties.put(64, Properties.alwaysFalse(ObjectProperty.CAST_SHADOW, true));

		properties.put(65, Properties.unsignedShort(ObjectProperty.SCALE_X, ConfigConstants.DEFAULT_SCALE));
		properties.put(66, Properties.unsignedShort(ObjectProperty.SCALE_Y, ConfigConstants.DEFAULT_SCALE));
		properties.put(67, Properties.unsignedShort(ObjectProperty.SCALE_Z, ConfigConstants.DEFAULT_SCALE));

		properties.put(68, Properties.unsignedShort(ObjectProperty.MAPSCENE, -1));
		properties.put(69, Properties.unsignedShort(ObjectProperty.SURROUNDINGS, 0));

		properties.put(70, Properties.unsignedShort(ObjectProperty.TRANSLATION_X, 0));
		properties.put(71, Properties.unsignedShort(ObjectProperty.TRANSLATION_Y, 0));
		properties.put(72, Properties.unsignedShort(ObjectProperty.TRANSLATION_Z, 0));

		properties.put(73, Properties.alwaysTrue(ObjectProperty.OBSTRUCTIVE_GROUND, false));
		properties.put(74, Properties.alwaysTrue(ObjectProperty.HOLLOW, false));
		properties.put(75, Properties.unsignedByte(ObjectProperty.SUPPORTS_ITEMS, 0));

		properties.put(106, new DefinitionProperty<>(ObjectProperty.MORPHISM_SET, MorphismSet.EMPTY, MorphismSet::encode,
				MorphismSet::decode, morphisms -> Short.BYTES * (2 + morphisms.getCount()) + Byte.BYTES));

		return properties;
	}
}