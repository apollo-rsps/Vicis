package rs.emulate.legacy.config.floor;

import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

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
	 * Gets the {@link SerializableProperty} containing the colour of this FloorDefinition, in rgb form.
	 *
	 * @return The ConfigProperty containing the rgb colour.
	 */
	public SerializableProperty<Integer> colour() {
		return properties.get(FloorProperty.COLOUR);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the minimap colour of this FloorDefinition, in rgb form.
	 *
	 * @return The ConfigProperty containing the rgb minimap colour.
	 */
	public SerializableProperty<Integer> minimapColour() {
		return properties.get(FloorProperty.MINIMAP_COLOUR);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the name of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the name.
	 */
	public SerializableProperty<String> name() {
		return properties.get(FloorProperty.NAME);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the shadowed flag of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the shadowed flag.
	 */
	public SerializableProperty<Boolean> shadowed() {
		return properties.get(FloorProperty.SHADOWED);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the texture id of this FloorDefinition.
	 *
	 * @return The ConfigProperty containing the texture id.
	 */
	public SerializableProperty<Integer> texture() {
		return properties.get(FloorProperty.TEXTURE);
	}

}