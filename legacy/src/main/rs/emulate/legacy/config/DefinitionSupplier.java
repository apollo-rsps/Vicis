package rs.emulate.legacy.config;

import java.lang.reflect.Constructor;

import rs.emulate.legacy.archive.ArchiveUtils;

/**
 * Supplies {@link MutableConfigDefinition}s and {@link DefaultConfigDefinition}s.
 *
 * @author Major
 * @param <T> The MutableConfigDefinition type.
 */
public final class DefinitionSupplier<T extends MutableConfigDefinition> {

	/**
	 * Creates a DefinitionSupplier.
	 *
	 * @param name The name of the ArchiveEntry containing the definitions, <strong>without</strong> an extension.
	 * @param definition The definition {@link Class} type.
	 * @param defaultClass The {@link DefaultConfigDefinition} Class type.
	 * @return The MutableConfigDefinition of type {@code T}.
	 */
	public static <T extends MutableConfigDefinition> DefinitionSupplier<T> create(String name, Class<T> definition,
			Class<? extends DefaultConfigDefinition<T>> defaultClass) {
		try {
			Constructor<T> constructor = definition.getConstructor(int.class, ConfigPropertyMap.class);
			DefaultConfigDefinition<T> immutable = defaultClass.newInstance();

			return new DefinitionSupplier<>(name, definition, constructor, immutable);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Failed to create an instance of a MutableConfigDefinition."
					+ "Please ensure that your configuration file is correct, and that your custom "
					+ "DefaultConfigDefinition(s) have an appropriate (id, ConfigPropertyMap) constructor "
					+ " (if applicable).", exception);
		}
	}

	/**
	 * The Constructor used to create new definitions.
	 */
	private final Constructor<T> constructor;

	/**
	 * The DefaultConfigDefinition used as a base for new definitions.
	 */
	private final DefaultConfigDefinition<T> immutable;

	/**
	 * The name of the ArchiveEntry.
	 */
	private final String name;

	/**
	 * The Class type of the definition.
	 */
	private final Class<T> type;

	/**
	 * Creates the DefinitionSupplier.
	 *
	 * @param name The name of the ArchiveEntry, <strong>without</strong> an extension.
	 * @param type The {@link Class} type of the {@link MutableConfigDefinition}.
	 * @param constructor The {@link Constructor} used to create new MutableConfigDefinitions.
	 * @param immutable The {@link DefaultConfigDefinition} used as a base for new MutableConfigDefinitions.
	 */
	private DefinitionSupplier(String name, Class<T> type, Constructor<T> constructor,
			DefaultConfigDefinition<T> immutable) {
		this.name = name;
		this.type = type;
		this.constructor = constructor;
		this.immutable = immutable;
	}

	/**
	 * Creates a new {@link MutableConfigDefinition}.
	 *
	 * @param id The id.
	 * @param map The {@link ConfigPropertyMap}.
	 * @return The MutableDefinition.
	 */
	public T create(int id, ConfigPropertyMap map) {
		try {
			return constructor.newInstance(id, map);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Failed to create a new definition, please report: ", exception);
		}
	}

	/**
	 * Gets a default {@link ConfigPropertyMap} from the supplier.
	 *
	 * @return The ConfigPropertyMap.
	 */
	public ConfigPropertyMap getDefault() {
		return immutable.toPropertyMap();
	}

	/**
	 * Gets the identifier of the ArchiveEntry (see {@link ArchiveUtils#hash}).
	 *
	 * @return The identifier.
	 */
	public int getIdentifier() {
		return ArchiveUtils.hash(name);
	}

	/**
	 * Gets the name of the ArchiveEntry.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the {@link Class} type of this DefinitionSupplier.
	 *
	 * @return The Class type.
	 */
	public Class<T> getType() {
		return type;
	}

}