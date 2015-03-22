package rs.emulate.legacy.config;

import java.util.Map;

import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

import com.google.common.collect.ImmutableMap;

/**
 * A base class for default definitions. All default definitions <strong>must</strong> be <strong>immutable</strong>.
 * 
 * @author Major
 */
public abstract class DefaultConfigDefinition {

	/**
	 * The map of opcodes to DefinitionProperty objects.
	 */
	private final Map<Integer, DefinitionProperty<?>> properties;

	/**
	 * Creates the DefaultDefinition.
	 */
	protected DefaultConfigDefinition() {
		this.properties = ImmutableMap.copyOf(init());
	}

	/**
	 * Gets a {@link PropertyMap}
	 * 
	 * @return The PropertyMap.
	 */
	public final PropertyMap toPropertyMap() {
		return new PropertyMap(properties);
	}

	/**
	 * Initialises the DefaultDefinition.
	 * 
	 * @return The {@link Map} of opcodes to {@link DefinitionProperty} objects.
	 */
	protected abstract Map<Integer, DefinitionProperty<?>> init();

}