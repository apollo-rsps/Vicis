package rs.emulate.legacy.config.object;

import java.util.Map;

import rs.emulate.legacy.config.MutableDefinition;
import rs.emulate.legacy.config.npc.MorphismSet;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

/**
 * A {@link MutableDefinition} for an object.
 * 
 * @author Major
 */
public class ObjectDefinition extends MutableDefinition {

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
	 * @param properties The {@link PropertyMap}.
	 */
	public ObjectDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the ambient lighting factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> ambientLighting() {
		return getProperty(ObjectProperty.AMBIENT_LIGHTING);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the animation id.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> animation() {
		return getProperty(ObjectProperty.ANIMATION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the casts shadow flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> castShadow() {
		return getProperty(ObjectProperty.CAST_SHADOW);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the {@link Map} of original to replacement colours.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Map<Integer, Integer>> colours() {
		return getProperty(ObjectProperty.COLOURS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the contour ground flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> contourGround() {
		return getProperty(ObjectProperty.CONTOUR_GROUND);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the decor displacement value.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> decorDisplacement() {
		return getProperty(ObjectProperty.DECOR_DISPLACEMENT);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the delay shading flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> delayShading() {
		return getProperty(ObjectProperty.DELAY_SHADING);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the description.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<String> description() {
		return getProperty(ObjectProperty.DESCRIPTION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the hollow flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> hollow() {
		return getProperty(ObjectProperty.HOLLOW);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the impenetrable flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> impenetrable() {
		return getProperty(ObjectProperty.IMPENETRABLE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the interactive flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> interactive() {
		return getProperty(ObjectProperty.INTERACTIVE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the inverted flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> inverted() {
		return getProperty(ObjectProperty.INVERTED);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the length.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> length() {
		return getProperty(ObjectProperty.LENGTH);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the light diffusion factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> lightDiffusion() {
		return getProperty(ObjectProperty.LIGHT_DIFFUSION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the mapscene id.
	 * <p>
	 * This refers to the "mapscene" image displayed beneath the object when it is drawn on the map.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> mapscene() {
		return getProperty(ObjectProperty.MAPSCENE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the minimap function.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> minimapFunction() {
		return getProperty(ObjectProperty.MINIMAP_FUNCTION);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the unpositioned {@link ModelSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<ModelSet> models() {
		return getProperty(ObjectProperty.MODELS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the {@link MorphismSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<MorphismSet> morphismSet() {
		return getProperty(ObjectProperty.MORPHISM_SET);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the name.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<String> name() {
		return getProperty(ObjectProperty.NAME);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the obstructive ground flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> obstructiveGround() {
		return getProperty(ObjectProperty.OBSTRUCTIVE_GROUND);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the occlusion flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> occlude() {
		return getProperty(ObjectProperty.OCCLUDE);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the positioned {@link ModelSet}.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<ModelSet> positionedModels() {
		return getProperty(ObjectProperty.POSITIONED_MODELS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the width scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> scaleX() {
		return getProperty(ObjectProperty.SCALE_X);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the length scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> scaleY() {
		return getProperty(ObjectProperty.SCALE_Y);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the height scaling factor.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> scaleZ() {
		return getProperty(ObjectProperty.SCALE_Z);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the solid flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> solid() {
		return getProperty(ObjectProperty.SOLID);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the supports items flag, indicating whether or not the object
	 * allows items to be placed on it.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Boolean> supportsItems() {
		return getProperty(ObjectProperty.SUPPORTS_ITEMS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the surroundings flag.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> surroundings() {
		return getProperty(ObjectProperty.SURROUNDINGS);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the width translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> translationX() {
		return getProperty(ObjectProperty.TRANSLATION_X);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the length translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> translationY() {
		return getProperty(ObjectProperty.TRANSLATION_Y);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the height translation.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> translationZ() {
		return getProperty(ObjectProperty.TRANSLATION_Z);
	}

	/**
	 * Gets the {@link DefinitionProperty} containing the width.
	 * 
	 * @return The DefinitionProperty.
	 */
	public DefinitionProperty<Integer> width() {
		return getProperty(ObjectProperty.WIDTH);
	}

}