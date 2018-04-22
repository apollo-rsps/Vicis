package rs.emulate.legacy.config.`object`

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.npc.MorphismSet

/**
 * A [MutableConfigDefinition] for an object.
 */
open class ObjectDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val ambientLighting: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.AMBIENT_LIGHTING)

    val animation: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.ANIMATION)

    val castShadow: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.CAST_SHADOW)

    /**
     * The [Map] of original to replacement colours.
     */
    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(ObjectProperty.COLOURS)

    val contourGround: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.CONTOUR_GROUND)

    val decorDisplacement: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.DECOR_DISPLACEMENT)

    val delayShading: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.DELAY_SHADING)

    val description: SerializableProperty<String>
        get() = getProperty(ObjectProperty.DESCRIPTION)

    val hollow: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.HOLLOW)

    val impenetrable: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.IMPENETRABLE)

    val interactive: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.INTERACTIVE)

    val inverted: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.INVERTED)

    val length: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.LENGTH)

    val lightDiffusion: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.LIGHT_DIFFUSION)

    /**
     * Gets the id of the "mapscene" image displayed beneath the object when it is drawn on the map.
     */
    val mapscene: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.MAPSCENE)

    val minimapFunction: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.MINIMAP_FUNCTION)

    val models: SerializableProperty<ModelSet>
        get() = getProperty(ObjectProperty.MODELS)

    val morphismSet: SerializableProperty<MorphismSet>
        get() = getProperty(ObjectProperty.MORPHISM_SET)

    val name: SerializableProperty<String>
        get() = getProperty(ObjectProperty.NAME)

    val obstructiveGround: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.OBSTRUCTIVE_GROUND)

    val occlude: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.OCCLUDE)

    val positionedModels: SerializableProperty<ModelSet>
        get() = getProperty(ObjectProperty.POSITIONED_MODELS)

    val scaleX: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.SCALE_X)

    val scaleY: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.SCALE_Y)

    val scaleZ: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.SCALE_Z)

    val solid: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.SOLID)

    /**
     * Whether or not the object allows items to be placed on it. // TODO wrong?
     */
    val supportsItems: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.SUPPORTS_ITEMS)

    val surroundings: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.SURROUNDINGS)

    val translationX: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TRANSLATION_X)

    val translationY: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TRANSLATION_Y)

    val translationZ: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TRANSLATION_Z)

    val width: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.WIDTH)

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
