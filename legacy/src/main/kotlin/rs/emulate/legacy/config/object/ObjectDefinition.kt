package rs.emulate.legacy.config.`object`

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.npc.MorphismSet

/**
 * A [MutableConfigDefinition] for an object.
 *
 * @param id The id.
 * @param properties The [ConfigPropertyMap].
 */
open class ObjectDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the ambient lighting factor.
     */
    fun ambientLighting(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.AMBIENT_LIGHTING)
    }

    /**
     * Gets the [SerializableProperty] containing the animation id.
     */
    fun animation(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.ANIMATION)
    }

    /**
     * Gets the [SerializableProperty] containing the casts shadow flag.
     */
    fun castShadow(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.CAST_SHADOW)
    }

    /**
     * Gets the [SerializableProperty] containing the [Map] of original to replacement colours.
     */
    fun colours(): SerializableProperty<Map<Int, Int>> {
        return getProperty(ObjectProperty.COLOURS)
    }

    /**
     * Gets the [SerializableProperty] containing the contour ground flag.
     */
    fun contourGround(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.CONTOUR_GROUND)
    }

    /**
     * Gets the [SerializableProperty] containing the decor displacement value.
     */
    fun decorDisplacement(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.DECOR_DISPLACEMENT)
    }

    /**
     * Gets the [SerializableProperty] containing the delay shading flag.
     */
    fun delayShading(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.DELAY_SHADING)
    }

    /**
     * Gets the [SerializableProperty] containing the description.
     */
    fun description(): SerializableProperty<String> {
        return getProperty(ObjectProperty.DESCRIPTION)
    }

    /**
     * Gets the [SerializableProperty] containing the hollow flag.
     */
    fun hollow(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.HOLLOW)
    }

    /**
     * Gets the [SerializableProperty] containing the impenetrable flag.
     */
    fun impenetrable(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.IMPENETRABLE)
    }

    /**
     * Gets the [SerializableProperty] containing the interactive flag.
     */
    fun interactive(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.INTERACTIVE)
    }

    /**
     * Gets the [SerializableProperty] containing the inverted flag.
     */
    fun inverted(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.INVERTED)
    }

    /**
     * Gets the [SerializableProperty] containing the length.
     */
    fun length(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.LENGTH)
    }

    /**
     * Gets the [SerializableProperty] containing the light diffusion factor.
     */
    fun lightDiffusion(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.LIGHT_DIFFUSION)
    }

    /**
     * Gets the [SerializableProperty] containing the mapscene id.
     *
     * This refers to the "mapscene" image displayed beneath the object when it is drawn on the map.
     */
    fun mapscene(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.MAPSCENE)
    }

    /**
     * Gets the [SerializableProperty] containing the minimap function.
     */
    fun minimapFunction(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.MINIMAP_FUNCTION)
    }

    /**
     * Gets the [SerializableProperty] containing the unpositioned [ModelSet].
     */
    fun models(): SerializableProperty<ModelSet> {
        return getProperty(ObjectProperty.MODELS)
    }

    /**
     * Gets the [SerializableProperty] containing the [MorphismSet].
     */
    fun morphismSet(): SerializableProperty<MorphismSet> {
        return getProperty(ObjectProperty.MORPHISM_SET)
    }

    /**
     * Gets the [SerializableProperty] containing the name.
     */
    fun name(): SerializableProperty<String> {
        return getProperty(ObjectProperty.NAME)
    }

    /**
     * Gets the [SerializableProperty] containing the obstructive ground flag.
     */
    fun obstructiveGround(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.OBSTRUCTIVE_GROUND)
    }

    /**
     * Gets the [SerializableProperty] containing the occlusion flag.
     */
    fun occlude(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.OCCLUDE)
    }

    /**
     * Gets the [SerializableProperty] containing the positioned [ModelSet].
     */
    fun positionedModels(): SerializableProperty<ModelSet> {
        return getProperty(ObjectProperty.POSITIONED_MODELS)
    }

    /**
     * Gets the [SerializableProperty] containing the width scaling factor.
     */
    fun scaleX(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.SCALE_X)
    }

    /**
     * Gets the [SerializableProperty] containing the length scaling factor.
     */
    fun scaleY(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.SCALE_Y)
    }

    /**
     * Gets the [SerializableProperty] containing the height scaling factor.
     */
    fun scaleZ(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.SCALE_Z)
    }

    /**
     * Gets the [SerializableProperty] containing the solid flag.
     */
    fun solid(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.SOLID)
    }

    /**
     * Gets the [SerializableProperty] containing the supports items flag, indicating whether or not the object
     * allows items to be placed on it.
     */
    fun supportsItems(): SerializableProperty<Boolean> {
        return getProperty(ObjectProperty.SUPPORTS_ITEMS)
    }

    /**
     * Gets the [SerializableProperty] containing the surroundings flag.
     */
    fun surroundings(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.SURROUNDINGS)
    }

    /**
     * Gets the [SerializableProperty] containing the width translation.
     */
    fun translationX(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.TRANSLATION_X)
    }

    /**
     * Gets the [SerializableProperty] containing the length translation.
     */
    fun translationY(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.TRANSLATION_Y)
    }

    /**
     * Gets the [SerializableProperty] containing the height translation.
     */
    fun translationZ(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.TRANSLATION_Z)
    }

    /**
     * Gets the [SerializableProperty] containing the width.
     */
    fun width(): SerializableProperty<Int> {
        return getProperty(ObjectProperty.WIDTH)
    }

    companion object {

        /**
         * The name of the ArchiveEntry containing the ObjectDefinitions, without the extension.
         */
        const val ENTRY_NAME = "loc"

        /**
         * The amount of interactions.
         */
        const val INTERACTION_COUNT = 10

        /**
         * The prefix used for interaction definition properties.
         */
        const val INTERACTION_PROPERTY_PREFIX = "interaction"
    }

}
