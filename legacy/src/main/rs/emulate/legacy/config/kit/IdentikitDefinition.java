package rs.emulate.legacy.config.kit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

/**
 * A definition for an identikit.
 *
 * @author Major
 */
public class IdentikitDefinition extends MutableConfigDefinition {

	/**
	 * The name of the archive entry containing the encoded IdentikitDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "idk";

	/**
	 * The amount of original and replacement colours.
	 */
	protected static final int COLOUR_COUNT = 10;

	/**
	 * The amount of head models.
	 */
	protected static final int HEAD_MODEL_COUNT = 10;

	/**
	 * The prefix for head models.
	 */
	protected static final String HEAD_MODEL_PREFIX = "head-model";

	/**
	 * Creates the IdentikitDefinition.
	 *
	 * @param id The id of the definition.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public IdentikitDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the body model ids of this IdentikitDefinition.
	 *
	 * @return The {@link IdentikitProperty#MODELS} ConfigProperty.
	 */
	public SerializableProperty<int[]> getBodyModels() {
		return getProperty(IdentikitProperty.MODELS);
	}

	/**
	 * Gets an {@link ImmutableMap} containing the original and replacement colour values of this IdentikitDefinition.
	 *
	 * @return The ImmutableMap.
	 */
	public final ImmutableMap<Integer, Integer> getColours() {
		ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			SerializableProperty<Integer> original = getProperty(ConfigUtils.getOriginalColourPropertyName(slot));
			SerializableProperty<Integer> replacement = getProperty(ConfigUtils.getReplacementColourPropertyName(slot));

			builder.put(original.getValue(), replacement.getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link SerializableProperty} containing the specified head model id of this IdentikitDefinition.
	 *
	 * @param model The model id.
	 * @return The head model ConfigProperty.
	 */
	public SerializableProperty<Integer> getHeadModel(int model) {
		return getProperty(ConfigUtils.newOptionProperty(IdentikitDefinition.HEAD_MODEL_PREFIX, model));
	}

	/**
	 * Gets an {@link ImmutableList} containing the head model ids of this IdentikitDefinition.
	 *
	 * @return The ImmutableList.
	 */
	public ImmutableList<Integer> getHeadModels() {
		ImmutableList.Builder<Integer> builder = ImmutableList.builder();

		for (int id = 1; id <= HEAD_MODEL_COUNT; id++) {
			builder.add(getHeadModel(id).getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link SerializableProperty} containing the part of this IdentikitDefinition.
	 *
	 * @return The {@link IdentikitProperty#PART} ConfigProperty.
	 */
	public SerializableProperty<Part> getPart() {
		return getProperty(IdentikitProperty.PART);
	}

	/**
	 * Returns the {@link SerializableProperty} containing whether or not this IdentikitDefinition can be used when
	 * designing
	 * a player.
	 *
	 * @return The {@link IdentikitProperty#PLAYER_DESIGN_STYLE} ConfigProperty.
	 */
	public SerializableProperty<Boolean> isPlayerDesignStyle() {
		return getProperty(IdentikitProperty.PLAYER_DESIGN_STYLE);
	}

}