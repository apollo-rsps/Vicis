package rs.emulate.legacy.config.floor;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;

/**
 * A definition for a Floor.
 *
 * @author Major
 */
public class FloorDefinition extends MutableConfigDefinition {

	/**
	 * Creates the FloorDefinition.
	 *
	 * @param id The id of the definition.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public FloorDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the colour of this FloorDefinition, in rgb form.
	 *
	 * @return The ConfigProperty containing the rgb colour.
	 */
	public ConfigProperty<Integer> colour() {
		return properties.get(FloorProperty.COLOUR);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the minimap colour of this FloorDefinition, in rgb form.
	 *
	 * @return The ConfigProperty containing the rgb minimap colour.
	 */
	public ConfigProperty<Integer> minimapColour() {
		return properties.get(FloorProperty.MINIMAP_COLOUR);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the name of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the name.
	 */
	public ConfigProperty<String> name() {
		return properties.get(FloorProperty.NAME);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the shadowed flag of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the shadowed flag.
	 */
	public ConfigProperty<Boolean> shadowed() {
		return properties.get(FloorProperty.SHADOWED);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the texture id of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the texture id.
	 */
	public ConfigProperty<Integer> texture() {
		return properties.get(FloorProperty.TEXTURE);
	}

}