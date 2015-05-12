package rs.emulate.legacy.config;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * A base class for default definitions. All default definitions <strong>must</strong> be <strong>immutable</strong>.
 *
 * @author Major
 * @param <T> The type of definition this default is for.
 */
public abstract class DefaultConfigDefinition<T extends MutableConfigDefinition> {

	/**
	 * The map of opcodes to DefinitionProperty objects.
	 */
	private final Map<Integer, ConfigProperty<?>> properties;

	/**
	 * Creates the DefaultDefinition.
	 */
	protected DefaultConfigDefinition() {
		this.properties = ImmutableMap.copyOf(init());
	}

	/**
	 * Gets a {@link ConfigPropertyMap}
	 *
	 * @return The PropertyMap.
	 */
	public final ConfigPropertyMap toPropertyMap() {
		return new ConfigPropertyMap(properties);
	}

	/**
	 * Initialises the DefaultDefinition.
	 *
	 * @return The {@link Map} of opcodes to {@link ConfigProperty} objects.
	 */
	protected abstract Map<Integer, ConfigProperty<?>> init();

}