package rs.emulate.legacy.config

import rs.emulate.legacy.archive.ArchiveUtils

import java.lang.reflect.Constructor

/**
 * Supplies [MutableConfigDefinition]s and [DefaultConfigDefinition]s.
 *
 * @param name The name of the ArchiveEntry, **without** an extension.
 * @param type The [Class] type of the [MutableConfigDefinition].
 * @param constructor The [Constructor] used to create new MutableConfigDefinitions.
 * @param immutable The [DefaultConfigDefinition] used as a base for new MutableConfigDefinitions.
 *
 * @param T The MutableConfigDefinition type.
 */
class DefinitionSupplier<T : MutableConfigDefinition> private constructor(
    val name: String,
    val type: Class<T>,
    private val constructor: Constructor<T>,
    private val immutable: DefaultConfigDefinition<T>
) {

    /**
     * Gets a default [ConfigPropertyMap] from the supplier.
     */
    val default: ConfigPropertyMap
        get() = immutable.toPropertyMap()

    /**
     * Gets the identifier of the ArchiveEntry (see [ArchiveUtils.hash]).
     */
    val identifier: Int
        get() = ArchiveUtils.hash(name)

    /**
     * Creates a new [MutableConfigDefinition].
     */
    fun create(id: Int, map: ConfigPropertyMap): T {
        try {
            return constructor.newInstance(id, map)
        } catch (exception: ReflectiveOperationException) {
            throw IllegalStateException("Failed to create a new definition, please report: ", exception)
        }

    }

    companion object {

        /**
         * Creates a DefinitionSupplier.
         *
         * @param name The name of the ArchiveEntry containing the definitions, **without** an extension.
         * @param definition The definition [Class] type.
         * @param defaultClass The [DefaultConfigDefinition] Class type.
         * @return The MutableConfigDefinition of type `T`.
         */
        fun <T : MutableConfigDefinition> create(
            name: String,
            definition: Class<T>,
            defaultClass: Class<out DefaultConfigDefinition<T>>
        ): DefinitionSupplier<T> {
            try {
                val constructor = definition.getConstructor(Int::class.javaPrimitiveType, ConfigPropertyMap::class.java)
                val immutable = defaultClass.newInstance()

                return DefinitionSupplier(name, definition, constructor, immutable)
            } catch (exception: ReflectiveOperationException) {
                throw IllegalStateException("""
                    Failed to create a DefinitionSupplier. Please ensure that your configuration file is correct, and
                    that your custom DefaultConfigDefinition(s) have an appropriate (id, ConfigPropertyMap) constructor
                    (if applicable).
                    """.trimIndent(), exception)
            }
        }
    }

}
