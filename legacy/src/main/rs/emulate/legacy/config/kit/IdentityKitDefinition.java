package rs.emulate.legacy.config.kit;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.MutableConfigDefinition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A definition for an identity kit.
 *
 * @author Major
 */
public class IdentityKitDefinition extends MutableConfigDefinition {

	/**
	 * The name of the archive entry containing the encoded IdentityKitDefinitions, without the extension.
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
	 * Creates the IdentityKitDefinition.
	 * 
	 * @param id The id of the definition.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public IdentityKitDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the body model ids of this IdentityKitDefinition.
	 *
	 * @return The {@link IdentityKitProperty#MODELS} ConfigProperty.
	 */
	public ConfigProperty<int[]> getBodyModels() {
		return getProperty(IdentityKitProperty.MODELS);
	}

	/**
	 * Gets an {@link ImmutableMap} containing the original and replacement colour values of this IdentityKitDefinition.
	 *
	 * @return The ImmutableMap.
	 */
	public final ImmutableMap<Integer, Integer> getColours() {
		ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			ConfigProperty<Integer> original = getProperty(ConfigUtils.getOriginalColourPropertyName(slot));
			ConfigProperty<Integer> replacement = getProperty(ConfigUtils.getReplacementColourPropertyName(slot));

			builder.put(original.getValue(), replacement.getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link ConfigProperty} containing the specified head model id of this IdentityKitDefinition.
	 * 
	 * @param model The model id.
	 * @return The head model ConfigProperty.
	 */
	public ConfigProperty<Integer> getHeadModel(int model) {
		return getProperty(ConfigUtils.newOptionProperty(IdentityKitDefinition.HEAD_MODEL_PREFIX, model));
	}

	/**
	 * Gets an {@link ImmutableList} containing the head model ids of this IdentityKitDefinition.
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
	 * Gets the {@link ConfigProperty} containing the part of this IdentityKitDefinition.
	 *
	 * @return The {@link IdentityKitProperty#PART} ConfigProperty.
	 */
	public ConfigProperty<Part> getPart() {
		return getProperty(IdentityKitProperty.PART);
	}

	/**
	 * Returns the {@link ConfigProperty} containing whether or not this IdentityKitDefinition can be used when designing a
	 * player.
	 *
	 * @return The {@link IdentityKitProperty#PLAYER_DESIGN_STYLE} ConfigProperty.
	 */
	public ConfigProperty<Boolean> isPlayerDesignStyle() {
		return getProperty(IdentityKitProperty.PLAYER_DESIGN_STYLE);
	}

}