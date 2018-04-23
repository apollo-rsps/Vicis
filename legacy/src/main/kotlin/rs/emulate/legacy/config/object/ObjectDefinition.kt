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
        get() = getProperty(ObjectProperty.AmbientLighting)

    val animation: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.Animation)

    val castShadow: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.CastShadow)

    /**
     * The [Map] of original to replacement colours.
     */
    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(ObjectProperty.Colours)

    val contourGround: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.ContourGround)

    val decorDisplacement: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.DecorDisplacement)

    val delayShading: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.DelayShading)

    val description: SerializableProperty<String>
        get() = getProperty(ObjectProperty.Description)

    val hollow: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Hollow)

    val impenetrable: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Impenetrable)

    val interactive: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Interactive)

    val inverted: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Inverted)

    val length: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.Length)

    val lightDiffusion: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.LightDiffusion)

    /**
     * Gets the id of the "mapscene" image displayed beneath the object when it is drawn on the map.
     */
    val mapscene: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.Mapscene)

    val minimapFunction: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.MinimapFunction)

    val models: SerializableProperty<ModelSet>
        get() = getProperty(ObjectProperty.Models)

    val morphismSet: SerializableProperty<MorphismSet>
        get() = getProperty(ObjectProperty.Morphisms)

    val name: SerializableProperty<String>
        get() = getProperty(ObjectProperty.Name)

    val obstructiveGround: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.ObstructiveGround)

    val occlude: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Occlude)

    val positionedModels: SerializableProperty<ModelSet>
        get() = getProperty(ObjectProperty.PositionedModels)

    val scaleX: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.ScaleX)

    val scaleY: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.ScaleY)

    val scaleZ: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.ScaleZ)

    val solid: SerializableProperty<Boolean>
        get() = getProperty(ObjectProperty.Solid)

    /**
     * Whether or not the object allows items to be placed on it. // TODO wrong?
     */
    val supportsItems: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.SupportsItems)

    val surroundings: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.Surroundings)

    val translationX: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TranslationX)

    val translationY: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TranslationY)

    val translationZ: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.TranslationZ)

    val width: SerializableProperty<Int>
        get() = getProperty(ObjectProperty.Width)

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
        const val INTERACTION_PROPERTY_PREFIX = "Interaction"
    }

}
