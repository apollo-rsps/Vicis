package rs.emulate.legacy.config.kit;

import java.util.List;

import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A definition for an identity kit.
 *
 * @author Major
 */
public class IdentityKitDefinition extends MutableConfigDefinition {

	/**
	 * The name of the archive entry containing the identity kit definitions, without the extension.
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
	 * Creates the identity kit definition.
	 * 
	 * @param id The id of this definition.
	 * @param properties The {@link PropertyMap}.
	 */
	public IdentityKitDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link List} of body model ids of this identity kit.
	 *
	 * @return The property containing the body model ids.
	 */
	public DefinitionProperty<int[]> getBodyModels() {
		return getProperty(IdentityKitProperty.MODELS);
	}

	/**
	 * Gets an {@link ImmutableMap} containing the original and replacement colour values.
	 *
	 * @return The map.
	 */
	public final ImmutableMap<Integer, Integer> getColours() {
		ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			DefinitionProperty<Integer> original = getProperty(ConfigDefinitionUtils.getOriginalColourPropertyName(slot));
			DefinitionProperty<Integer> replacement = getProperty(ConfigDefinitionUtils.getReplacementColourPropertyName(slot));

			builder.put(original.getValue(), replacement.getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the specified head model id of this identity kit.
	 * 
	 * @param model The model id.
	 * @return The property containing the head model id.
	 */
	public DefinitionProperty<Integer> getHeadModel(int model) {
		return getProperty(ConfigDefinitionUtils.createOptionProperty(IdentityKitDefinition.HEAD_MODEL_PREFIX, model));
	}

	/**
	 * Gets an {@link ImmutableList} containing the head model ids of this definition.
	 *
	 * @return The list.
	 */
	public ImmutableList<Integer> getHeadModels() {
		ImmutableList.Builder<Integer> builder = ImmutableList.builder();

		for (int id = 1; id <= HEAD_MODEL_COUNT; id++) {
			builder.add(getHeadModel(id).getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the part of this identity kit.
	 *
	 * @return The property containing the part.
	 */
	public DefinitionProperty<Part> getPart() {
		return getProperty(IdentityKitProperty.PART);
	}

	/**
	 * Returns the {@link DefinitionProperty} containing whether or not this identity kit can be used when designing an
	 * avatar.
	 *
	 * @return The property containing the is player style flag.
	 */
	public DefinitionProperty<Boolean> isPlayerDesignStyle() {
		return getProperty(IdentityKitProperty.PLAYER_DESIGN_STYLE);
	}

}