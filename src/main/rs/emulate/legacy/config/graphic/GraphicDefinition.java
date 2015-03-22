package rs.emulate.legacy.config.graphic;

import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.util.Assertions;

import com.google.common.collect.ImmutableMap;

/**
 * A definition for a graphic (a 'spotanim').
 * 
 * @author Major
 */
public class GraphicDefinition extends MutableConfigDefinition {

	/**
	 * The name of the archive entry containing the graphic definitions, without the extension.
	 */
	public static final String ENTRY_NAME = "spotanim";

	/**
	 * The amount of original and replacement colours.
	 */
	protected static final int COLOUR_COUNT = 10;

	/**
	 * Checks that the specified slot for a colour is valid (i.e. {@code 1 <= slot <=} {@link #COLOUR_COUNT}).
	 * 
	 * @param slot The slot.
	 * @throws IllegalArgumentException If the slot is invalid.
	 */
	private static void checkColourBounds(int slot) {
		Assertions.checkWithin(1, COLOUR_COUNT, slot, "Slot must be greater than zero and less than eleven.");
	}

	/**
	 * Creates the GraphicDefinition.
	 *
	 * @param id The id of the definition.
	 * @param properties The {@link PropertyMap}.
	 */
	public GraphicDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the animation id of this graphic.
	 *
	 * @return The animation id.
	 */
	public DefinitionProperty<Integer> animation() {
		return getProperty(GraphicProperty.ANIMATION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the model brightness of this graphic.
	 *
	 * @return The brightness.
	 */
	public DefinitionProperty<Integer> brightness() {
		return getProperty(GraphicProperty.BRIGHTNESS);
	}

	/**
	 * Gets an {@link ImmutableMap} containing the original and replacement colour values.
	 *
	 * @return The map.
	 */
	public ImmutableMap<Integer, Integer> colours() {
		ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			DefinitionProperty<Integer> original = originalColour(slot);
			DefinitionProperty<Integer> replacement = replacementColour(slot);
			builder.put(original.getValue(), replacement.getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the depth scale of this graphic.
	 *
	 * @return The depth scale.
	 */
	public DefinitionProperty<Integer> depthScale() {
		return getProperty(GraphicProperty.DEPTH_SCALE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the model id of this graphic.
	 *
	 * @return The model id.
	 */
	public DefinitionProperty<Integer> modelId() {
		return getProperty(GraphicProperty.MODEL);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the original colour for the specified slot.
	 *
	 * @param slot The slot.
	 * @return The original colour.
	 */
	public DefinitionProperty<Integer> originalColour(int slot) {
		checkColourBounds(slot);
		return getProperty(ConfigDefinitionUtils.getOriginalColourPropertyName(slot));
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the replacement colour for the specified slot.
	 *
	 * @param slot The slot.
	 * @return The replacement colour.
	 */
	public DefinitionProperty<Integer> replacementColour(int slot) {
		checkColourBounds(slot);
		return getProperty(ConfigDefinitionUtils.getReplacementColourPropertyName(slot));
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the rotation of this graphic.
	 *
	 * @return The rotation.
	 */
	public DefinitionProperty<Integer> rotation() {
		return getProperty(GraphicProperty.ROTATION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the model shadow of this graphic.
	 *
	 * @return The shadow.
	 */
	public DefinitionProperty<Integer> shadow() {
		return getProperty(GraphicProperty.SHADOW);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the breadth scale of this graphic.
	 *
	 * @return The breadth scale.
	 */
	public DefinitionProperty<Integer> vreadthScale() {
		return getProperty(GraphicProperty.BREADTH_SCALE);
	}

}