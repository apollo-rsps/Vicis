package rs.emulate.legacy.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import rs.emulate.shared.property.DynamicPropertyType;
import rs.emulate.shared.util.DataBuffer;

/**
 * Contains utility methods used in config definitions.
 *
 * @author Major
 */
public final class ConfigUtils {

	/**
	 * The String prefixed when creating {@link DynamicPropertyType}s for original colours.
	 */
	private static final String ORIGINAL_COLOUR_PREFIX = "original-colour";

	/**
	 * The String prefixed when creating {@link DynamicPropertyType}s for replacement colours.
	 */
	private static final String REPLACEMENT_COLOUR_PREFIX = "replacement-colour";

	/**
	 * Creates a {@link ConfigProperty} for a {@link Map} of original to replacement colour values.
	 * <p>
	 * FIXME This property is too complicated to be done like this.
	 *
	 * @param type The {@link ConfigPropertyType} of the DefinitionProperty.
	 * @return The DefinitionProperty.
	 */
	public static <T extends ConfigPropertyType> ConfigProperty<Map<Integer, Integer>> newColourProperty(T type) {
		BiConsumer<DataBuffer, Map<Integer, Integer>> encoder = (buffer, colours) -> {
			buffer.putByte(colours.size());
			colours.entrySet().forEach(colour -> buffer.putShort(colour.getKey()).putShort(colour.getValue()));
		};

		Function<DataBuffer, Map<Integer, Integer>> decoder = buffer -> {
			int size = buffer.getUnsignedByte();
			Map<Integer, Integer> colours = new HashMap<>(size);

			for (int colour = 0; colour < size; colour++) {
				colours.put(buffer.getUnsignedShort(), buffer.getUnsignedShort());
			}

			return colours;
		};

		Function<String, Optional<Map<Integer, Integer>>> parser = input -> {
			return null; // XXX
		};

		return new ConfigProperty<>(type, new HashMap<Integer, Integer>(1), encoder, decoder, colours -> colours.size()
				* Short.BYTES * 2 + Byte.BYTES, parser);
	}

	/**
	 * Returns a {@link DynamicPropertyType} with the name of the form "{@code [prefix]}-{@code [slot]}".
	 * <p>
	 * The returned DynamicPropertyType may be the same object as a previously-returned one, as only one
	 * DynamicPropertyType may exist per string (see {@link DynamicPropertyType#valueOf}).
	 *
	 * @param prefix The prefix.
	 * @param option The option.
	 * @return The DynamicPropertyType.
	 */
	public static DynamicPropertyType newOptionProperty(String prefix, int option) {
		return DynamicPropertyType.valueOf(prefix + "-" + option, option);
	}

	/**
	 * Creates or retrieves a {@link DynamicPropertyType} for original colours, with a name of the form
	 * "original-colour-{@code [slot]}".
	 * <p>
	 * The returned DynamicPropertyType may be the same object as a previously-returned one, as only one
	 * DynamicPropertyType may exist per string (see {@link DynamicPropertyType#valueOf}).
	 *
	 * @param slot The colour slot.
	 * @return The DynamicPropertyType.
	 */
	public static DynamicPropertyType getOriginalColourPropertyName(int slot) {
		return newOptionProperty(ORIGINAL_COLOUR_PREFIX, slot);
	}

	/**
	 * Creates or retrieves a a {@link DynamicPropertyType} for replacement colours, with a name of the form
	 * "replacement-colour-{@code [slot]}".
	 * <p>
	 * The returned DynamicPropertyType may be the same object as a previously created one, as only one
	 * DynamicPropertyType may exist per string (see {@link DynamicPropertyType#valueOf}).
	 *
	 * @param slot The colour slot.
	 * @return The DynamicPropertyType.
	 */
	public static DynamicPropertyType getReplacementColourPropertyName(int slot) {
		return newOptionProperty(REPLACEMENT_COLOUR_PREFIX, slot);
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private ConfigUtils() {

	}

}