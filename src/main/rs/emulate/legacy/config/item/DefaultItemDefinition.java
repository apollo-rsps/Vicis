package rs.emulate.legacy.config.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.DefaultDefinition;
import rs.emulate.legacy.config.DefinitionUtils;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.DynamicPropertyType;
import rs.emulate.shared.prop.Properties;
import rs.emulate.shared.prop.PropertyDecoders;
import rs.emulate.shared.prop.PropertyEncoders;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.prop.PropertyType;

/**
 * A default {@link ItemDefinition} used as a base for actual definitions.
 * 
 * @author Major
 */
public class DefaultItemDefinition extends DefaultDefinition {

	/**
	 * The DefaultItemDefinition.
	 */
	private static final DefaultItemDefinition DEFAULT = new DefaultItemDefinition();

	/**
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultItemDefinition.
	 */
	private DefaultItemDefinition() {
		super();
	}

	@Override
	protected Map<Integer, DefinitionProperty<?>> init() {
		Map<Integer, DefinitionProperty<?>> properties = new HashMap<>();

		properties.put(1, Properties.unsignedShort(ItemProperty.MODEL, 0));
		properties.put(2, Properties.string(ItemProperty.NAME, "null"));
		properties.put(3, Properties.string(ItemProperty.DESCRIPTION, "null"));
		properties.put(4, Properties.unsignedShort(ItemProperty.SPRITE_SCALE, 2_000));
		properties.put(5, Properties.unsignedShort(ItemProperty.SPRITE_PITCH, 0));
		properties.put(6, Properties.unsignedShort(ItemProperty.SPRITE_CAMERA_ROLL, 0));

		properties.put(7, new DefinitionProperty<>(ItemProperty.SPRITE_TRANSLATE_X, 0, PropertyEncoders.SHORT_ENCODER,
				PropertyDecoders.SIGNED_SHORT_DECODER, Short.BYTES));
		properties.put(8, new DefinitionProperty<>(ItemProperty.SPRITE_TRANSLATE_Y, 0, PropertyEncoders.SHORT_ENCODER,
				PropertyDecoders.SIGNED_SHORT_DECODER, Short.BYTES));

		properties.put(10, Properties.unsignedShort(DynamicPropertyType.valueOf("unknown", 10), 0));
		properties.put(11, Properties.alwaysTrue(ItemProperty.STACKABLE, false));
		properties.put(12, Properties.unsignedInt(ItemProperty.VALUE, 1));
		properties.put(16, Properties.alwaysTrue(ItemProperty.MEMBERS, false));

		properties.put(23, new DefinitionProperty<>(ItemProperty.PRIMARY_MALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES));
		properties.put(24, Properties.unsignedShort(ItemProperty.SECONDARY_MALE_MODEL, 0));

		properties.put(25, new DefinitionProperty<>(ItemProperty.PRIMARY_FEMALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES));
		properties.put(26, Properties.unsignedShort(ItemProperty.SECONDARY_FEMALE_MODEL, 0));

		for (int action = 1; action <= ItemConstants.MENU_ACTION_COUNT; action++) {
			PropertyType type = DefinitionUtils.createOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 29, Properties.string(type, "hidden"));

			type = DefinitionUtils.createOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 34, Properties.string(type, "hidden"));
		}

		properties.put(40, DefinitionUtils.createColourProperty(ItemProperty.COLOURS));

		properties.put(78, Properties.unsignedShort(ItemProperty.TERTIARY_MALE_MODEL, 0));
		properties.put(79, Properties.unsignedShort(ItemProperty.TERTIARY_FEMALE_MODEL, 0));

		properties.put(90, Properties.unsignedShort(ItemProperty.PRIMARY_MALE_HEAD_PIECE, 0));
		properties.put(91, Properties.unsignedShort(ItemProperty.PRIMARY_FEMALE_HEAD_PIECE, 0));
		properties.put(92, Properties.unsignedShort(ItemProperty.SECONDARY_MALE_HEAD_PIECE, 0));
		properties.put(93, Properties.unsignedShort(ItemProperty.SECONDARY_FEMALE_HEAD_PIECE, 0));

		properties.put(95, Properties.unsignedShort(ItemProperty.SPRITE_CAMERA_YAW, 0));

		properties.put(97, Properties.unsignedShort(ItemProperty.NOTE_INFO_ID, 0));
		properties.put(98, Properties.unsignedShort(ItemProperty.NOTE_TEMPLATE_ID, 0));

		for (int option = 1; option <= ItemConstants.ITEM_STACK_COUNT; option++) {
			PropertyType type = DefinitionUtils.createOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, option);
			properties.put(option + 99, new DefinitionProperty<>(type, ItemStack.EMPTY, ItemStack::encode, ItemStack::decode,
					Short.BYTES * 2));
		}

		properties.put(110, Properties.unsignedShort(ItemProperty.GROUND_SCALE_X, DefinitionUtils.DEFAULT_SCALE));
		properties.put(111, Properties.unsignedShort(ItemProperty.GROUND_SCALE_Y, DefinitionUtils.DEFAULT_SCALE));
		properties.put(112, Properties.unsignedShort(ItemProperty.GROUND_SCALE_Z, DefinitionUtils.DEFAULT_SCALE));

		properties.put(113, Properties.signedByte(ItemProperty.LIGHT_AMBIENCE, 0));
		properties.put(114,
				new DefinitionProperty<>(ItemProperty.LIGHT_DIFFUSION, 0, (buffer, light) -> buffer.putByte(light / 5),
						buffer -> buffer.getByte() * 5, Byte.BYTES));

		properties.put(115, Properties.unsignedByte(ItemProperty.TEAM, 0));

		return properties;
	}

}