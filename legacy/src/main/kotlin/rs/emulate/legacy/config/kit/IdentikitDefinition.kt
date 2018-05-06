package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for an identikit.
 */
open class IdentikitDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val models: SerializableProperty<IntArray>
        get() = getProperty(IdentikitProperty.Models)

    val colours: Map<Int, Int>
        get() {
            val colours = mutableMapOf<Int, Int>()

            for (slot in 0 until COLOUR_COUNT) {
                val original = getProperty<Int>(ConfigUtils.getOriginalColourPropertyName(slot, 40))
                val replacement = getProperty<Int>(ConfigUtils.getReplacementColourPropertyName(slot, 50))

                colours[original.value] = replacement.value
            }

            return colours
        }

    val widgetModels: List<Int>
        get() {
            val models = mutableListOf<Int>()

            for (id in 0 until WIDGET_MODEL_COUNT) {
                val model = getWidgetModel(id).value
                if (model != -1) {
                    models.add(model)
                }
            }

            return models
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
    fun getWidgetModel(model: Int): SerializableProperty<Int> {
        return getProperty(ConfigUtils.newOptionProperty(IdentikitDefinition.WIDGET_MODEL_PREFIX, model, 60))
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
         * The amount of widget models.
         */
        const val WIDGET_MODEL_COUNT = 10

        /**
         * The prefix for widget models.
         */
        const val WIDGET_MODEL_PREFIX = "Widget Model"
    }

}
