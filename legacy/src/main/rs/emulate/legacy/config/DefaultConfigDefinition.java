package rs.emulate.legacy.config;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * A base class for default definitions. All default definitions <strong>must</strong> be <strong>immutable</strong>.
 *
 * @param <T> The type of definition this default is for.
 * @author Major
 */
public abstract class DefaultConfigDefinition<T extends MutableConfigDefinition> {

	/**
	 * The map of opcodes to DefinitionProperty objects.
	 */
	private final Map<Integer, SerializableProperty<?>> properties;

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
	 * @return The {@link Map} of opcodes to {@link SerializableProperty} objects.
	 */
	protected abstract Map<Integer, SerializableProperty<?>> init();

}