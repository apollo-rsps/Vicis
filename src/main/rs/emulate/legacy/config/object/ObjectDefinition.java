package rs.emulate.legacy.config.object;

import java.util.Map;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.legacy.config.npc.MorphismSet;

/**
 * A {@link MutableConfigDefinition} for an object.
 * 
 * @author Major
 */
public class ObjectDefinition extends MutableConfigDefinition {

	/**
	 * The name of the ArchiveEntry containing the ObjectDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "loc";

	/**
	 * The amount of interactions.
	 */
	protected static final int INTERACTION_COUNT = 10;

	/**
	 * The prefix used for interaction definition properties.
	 */
	protected static final String INTERACTION_PROPERTY_PREFIX = "interaction";

	/**
	 * Creates the ObjectDefinition.
	 * 
	 * @param id The id.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public ObjectDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the ambient lighting factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> ambientLighting() {
		return getProperty(ObjectProperty.AMBIENT_LIGHTING);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the animation id.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> animation() {
		return getProperty(ObjectProperty.ANIMATION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the casts shadow flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> castShadow() {
		return getProperty(ObjectProperty.CAST_SHADOW);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link Map} of original to replacement colours.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Map<Integer, Integer>> colours() {
		return getProperty(ObjectProperty.COLOURS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the contour ground flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> contourGround() {
		return getProperty(ObjectProperty.CONTOUR_GROUND);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the decor displacement value.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> decorDisplacement() {
		return getProperty(ObjectProperty.DECOR_DISPLACEMENT);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the delay shading flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> delayShading() {
		return getProperty(ObjectProperty.DELAY_SHADING);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the description.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<String> description() {
		return getProperty(ObjectProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the hollow flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> hollow() {
		return getProperty(ObjectProperty.HOLLOW);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the impenetrable flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> impenetrable() {
		return getProperty(ObjectProperty.IMPENETRABLE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the interactive flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> interactive() {
		return getProperty(ObjectProperty.INTERACTIVE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the inverted flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> inverted() {
		return getProperty(ObjectProperty.INVERTED);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the length.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> length() {
		return getProperty(ObjectProperty.LENGTH);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the light diffusion factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> lightDiffusion() {
		return getProperty(ObjectProperty.LIGHT_DIFFUSION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the mapscene id.
	 * <p>
	 * This refers to the "mapscene" image displayed beneath the object when it is drawn on the map.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> mapscene() {
		return getProperty(ObjectProperty.MAPSCENE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the minimap function.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> minimapFunction() {
		return getProperty(ObjectProperty.MINIMAP_FUNCTION);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the unpositioned {@link ModelSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<ModelSet> models() {
		return getProperty(ObjectProperty.MODELS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link MorphismSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<MorphismSet> morphismSet() {
		return getProperty(ObjectProperty.MORPHISM_SET);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the name.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<String> name() {
		return getProperty(ObjectProperty.NAME);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the obstructive ground flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> obstructiveGround() {
		return getProperty(ObjectProperty.OBSTRUCTIVE_GROUND);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the occlusion flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> occlude() {
		return getProperty(ObjectProperty.OCCLUDE);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the positioned {@link ModelSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<ModelSet> positionedModels() {
		return getProperty(ObjectProperty.POSITIONED_MODELS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the width scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> scaleX() {
		return getProperty(ObjectProperty.SCALE_X);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the length scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> scaleY() {
		return getProperty(ObjectProperty.SCALE_Y);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the height scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> scaleZ() {
		return getProperty(ObjectProperty.SCALE_Z);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the solid flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> solid() {
		return getProperty(ObjectProperty.SOLID);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the supports items flag, indicating whether or not the object
	 * allows items to be placed on it.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Boolean> supportsItems() {
		return getProperty(ObjectProperty.SUPPORTS_ITEMS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the surroundings flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> surroundings() {
		return getProperty(ObjectProperty.SURROUNDINGS);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the width translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> translationX() {
		return getProperty(ObjectProperty.TRANSLATION_X);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the length translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> translationY() {
		return getProperty(ObjectProperty.TRANSLATION_Y);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the height translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> translationZ() {
		return getProperty(ObjectProperty.TRANSLATION_Z);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the width.
	 * 
	 * @return The DefinitionProperty.
	 */
	public ConfigProperty<Integer> width() {
		return getProperty(ObjectProperty.WIDTH);
	}

}