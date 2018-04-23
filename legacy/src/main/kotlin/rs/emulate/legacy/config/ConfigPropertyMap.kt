package rs.emulate.legacy.config

/**
 * A [Map] wrapper containing methods to get properties using their opcode or name.
 *
 * @param opcodes The [Map] of opcodes to [SerializableProperty] objects.
 * @throws IllegalArgumentException If `opcodes` contains a mapping for opcode 0.
 */
class ConfigPropertyMap(opcodes: Map<Int, SerializableProperty<*>>) {

    /**
     * The Map of opcodes to ConfigProperty objects.
     */
    private val opcodes: MutableMap<Int, SerializableProperty<*>>

    init {
        require(!opcodes.containsKey(0)) { "Opcode 0 is reserved." }
        this.opcodes = opcodes.mapValues { it.value.duplicate() }.toMutableMap()
    }

    /**
     * Creates the ConfigPropertyMap, using an existing one as the base.
     */
    constructor(map: ConfigPropertyMap) : this(map.opcodes)

    /**
     * Gets the [SerializableProperty] with the specified [ConfigPropertyType].
     * @throws IllegalArgumentException If no ConfigProperty with the specified name exists.
     */
    operator fun <T> get(name: ConfigPropertyType<T>): SerializableProperty<T> {
        return validate(opcodes[name.opcode], name)
    }

    /**
     * Gets the [SerializableProperty] with the specified opcode.
     * @throws IllegalArgumentException If no ConfigProperty with the specified opcode exists.
     */
    operator fun <T> get(opcode: Int): SerializableProperty<T> {
        return validate(opcodes[opcode], opcode)
    }

    /**
     * Gets a [Set] containing [Map.Entry][Map.Entry] objects of opcodes to [SerializableProperty] objects.
     */
    fun serializableProperties(): Set<Map.Entry<Int, SerializableProperty<*>>> {
        return opcodes.entries
    }

    /**
     * Adds a [SerializableProperty] with the specified opcode.
     */
    fun put(opcode: Int, property: SerializableProperty<*>) {
        require(opcode.toLong() > 0) { "Error placing property - opcode must be positive." }
        opcodes[opcode] = property
    }

    /**
     * Gets the size of this PropertyMap.
     */
    fun size(): Int {
        return opcodes.size
    }

    /**
     * Gets a [Collection] containing the values of this ConfigPropertyMap (i.e. the [SerializableProperty] objects).
     */
    fun values(): Collection<SerializableProperty<*>> {
        return opcodes.values
    }

    /**
     * Validates the specified [SerializableProperty] (i.e. ensures that it is not `null`).
     * @throws IllegalArgumentException If the ConfigProperty is `null`.
     */
    private fun <T> validate(property: SerializableProperty<*>?, key: Any): SerializableProperty<T> {
        @Suppress("UNCHECKED_CAST")
        return requireNotNull(property) { "No property with a key of $key exists." } as SerializableProperty<T>
    }

}
