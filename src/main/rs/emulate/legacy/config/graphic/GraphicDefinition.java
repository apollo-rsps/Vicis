package rs.emulate.legacy.config.graphic;

import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.MutableConfigDefinition;
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
	 * Gets the {@link ConfigProperty} containing the animation id of this graphic.
	 *
	 * @return The animation id.
	 */
	public ConfigProperty<Integer> animation() {
		return getProperty(GraphicProperty.ANIMATION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the model brightness of this graphic.
	 *
	 * @return The brightness.
	 */
	public ConfigProperty<Integer> brightness() {
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
			ConfigProperty<Integer> original = originalColour(slot);
			ConfigProperty<Integer> replacement = replacementColour(slot);
			builder.put(original.getValue(), replacement.getValue());
		}

		return builder.build();
	}

	/**
	 * Gets the {@link ConfigProperty} containing the depth scale of this graphic.
	 *
	 * @return The depth scale.
	 */
	public ConfigProperty<Integer> depthScale() {
		return getProperty(GraphicProperty.DEPTH_SCALE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the model id of this graphic.
	 *
	 * @return The model id.
	 */
	public ConfigProperty<Integer> modelId() {
		return getProperty(GraphicProperty.MODEL);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the original colour for the specified slot.
	 *
	 * @param slot The slot.
	 * @return The original colour.
	 */
	public ConfigProperty<Integer> originalColour(int slot) {
		checkColourBounds(slot);
		return getProperty(ConfigDefinitionUtils.getOriginalColourPropertyName(slot));
	}

	/**
	 * Gets the {@link ConfigProperty} containing the replacement colour for the specified slot.
	 *
	 * @param slot The slot.
	 * @return The replacement colour.
	 */
	public ConfigProperty<Integer> replacementColour(int slot) {
		checkColourBounds(slot);
		return getProperty(ConfigDefinitionUtils.getReplacementColourPropertyName(slot));
	}

	/**
	 * Gets the {@link ConfigProperty} containing the rotation of this graphic.
	 *
	 * @return The rotation.
	 */
	public ConfigProperty<Integer> rotation() {
		return getProperty(GraphicProperty.ROTATION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the model shadow of this graphic.
	 *
	 * @return The shadow.
	 */
	public ConfigProperty<Integer> shadow() {
		return getProperty(GraphicProperty.SHADOW);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the breadth scale of this graphic.
	 *
	 * @return The breadth scale.
	 */
	public ConfigProperty<Integer> breadthScale() {
		return getProperty(GraphicProperty.BREADTH_SCALE);
	}

}