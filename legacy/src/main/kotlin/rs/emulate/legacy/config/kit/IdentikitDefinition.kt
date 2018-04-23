package rs.emulate.legacy.config.kit

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for an identikit.
 */
open class IdentikitDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the body model ids of this IdentikitDefinition.
     */
    val bodyModels: SerializableProperty<IntArray>
        get() = getProperty(IdentikitProperty.Models)

    /**
     * Gets an [ImmutableMap] containing the original and replacement colour values of this IdentikitDefinition.
     */
    val colours: ImmutableMap<Int, Int>
        get() {
            val builder = ImmutableMap.builder<Int, Int>()

            for (slot in 1..COLOUR_COUNT) {
                val original = getProperty<Int>(ConfigUtils.getOriginalColourPropertyName(slot))
                val replacement = getProperty<Int>(ConfigUtils.getReplacementColourPropertyName(slot))

                builder.put(original.value, replacement.value)
            }

            return builder.build()
        }

    /**
     * Gets an [ImmutableList] containing the head model ids of this IdentikitDefinition.
     */
    val headModels: ImmutableList<Int>
        get() {
            val builder = ImmutableList.builder<Int>()

            for (id in 1..HEAD_MODEL_COUNT) {
                builder.add(getHeadModel(id).value)
            }

            return builder.build()
        }

    /**
     * Gets the [SerializableProperty] containing the part of this IdentikitDefinition.
     */
    val part: SerializableProperty<Part>
        get() = getProperty(IdentikitProperty.Part)

    /**
     * Returns the [SerializableProperty] containing whether or not this IdentikitDefinition can be used when
     * designing a player.
     */
    val isPlayerDesignStyle: SerializableProperty<Boolean>
        get() = getProperty(IdentikitProperty.PlayerDesignStyle)

    /**
     * Gets the [SerializableProperty] containing the specified head model id of this IdentikitDefinition.
     */
    fun getHeadModel(model: Int): SerializableProperty<Int> {
        return getProperty(ConfigUtils.newOptionProperty(IdentikitDefinition.HEAD_MODEL_PREFIX, model))
    }

    companion object {

        /**
         * The name of the archive entry containing the encoded IdentikitDefinitions, without the extension.
         */
        const val ENTRY_NAME = "idk"

        /**
         * The amount of original and replacement colours.
         */
        const val COLOUR_COUNT = 10

        /**
         * The amount of head models.
         */
        const val HEAD_MODEL_COUNT = 10

        /**
         * The prefix for head models.
         */
        const val HEAD_MODEL_PREFIX = "Head Model"
    }

}
