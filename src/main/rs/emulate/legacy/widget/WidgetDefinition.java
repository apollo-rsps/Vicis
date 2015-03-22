package rs.emulate.legacy.widget;

import java.util.Map;

import rs.emulate.shared.prop.DefinitionProperty;

/**
 * A component of a graphical user interface.
 *
 * @author Major
 */
public class WidgetDefinition {

	/**
	 * The Map of WidgetProperty objects to DefinitionProperty objects.
	 */
	private final Map<WidgetProperty, DefinitionProperty<?>> properties;

	/**
	 * Creates the WidgetDefinition.
	 *
	 * @param properties Map of {@link WidgetProperty} objects to {@link DefinitionProperty} objects.
	 */
	public WidgetDefinition(Map<WidgetProperty, DefinitionProperty<?>> properties) {
		this.properties = properties;
	}

	/**
	 * Sets the value of the specified {@link WidgetProperty} in this WidgetDefinition to the specified
	 * {@link DefinitionProperty}.
	 * 
	 * @param type The {@link WidgetProperty} to set.
	 * @param value The {@link DefinitionProperty}.
	 */
	public void set(WidgetProperty type, DefinitionProperty<?> value) {
		properties.put(type, value);
	}

}