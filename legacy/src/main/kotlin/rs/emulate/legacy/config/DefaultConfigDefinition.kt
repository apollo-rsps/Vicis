package rs.emulate.legacy.config

import com.google.common.collect.ImmutableMap

/**
 * A base class for default definitions. All default definitions **must** be immutable.
 *
 * @param T The type of definition this default is for.
 */
abstract class DefaultConfigDefinition<T : MutableConfigDefinition> protected constructor() {

    /**
     * The map of opcodes to DefinitionProperty objects.
     */
    private val properties: Map<Int, SerializableProperty<*>> = ImmutableMap.copyOf(init()) // TODO by lazy

    /**
     * Gets a [ConfigPropertyMap]
     */
    fun toPropertyMap(): ConfigPropertyMap {
        return ConfigPropertyMap(properties)
    }

    /**
     * Initialises the DefaultDefinition.
     *
     * @return The [Map] of opcodes to [SerializableProperty] objects.
     */
    protected abstract fun init(): Map<Int, SerializableProperty<*>>

}
