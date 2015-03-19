package rs.emulate.legacy.config.object;

import rs.emulate.legacy.config.npc.MorphismSet;
import rs.emulate.shared.prop.PropertyType;

/**
 * Contains {@link PropertyType} implementations for {@link ObjectDefinition}s.
 * 
 * @author Major
 */
enum ObjectProperty implements PropertyType {

	/**
	 * The positioned models property.
	 */
	POSITIONED_MODELS(1),

	/**
	 * The name property.
	 */
	NAME(2),

	/**
	 * The description property.
	 */
	DESCRIPTION(3),

	/**
	 * The models property.
	 */
	MODELS(5),

	/**
	 * The width property.
	 */
	WIDTH(14),

	/**
	 * The length property.
	 */
	LENGTH(15),

	/**
	 * The solid property.
	 */
	SOLID(16),

	/**
	 * The impenetrable property.
	 */
	IMPENETRABLE(17),

	/**
	 * The interactive property.
	 */
	INTERACTIVE(18),

	/**
	 * The contour ground property.
	 */
	CONTOUR_GROUND(21),

	/**
	 * The delay shading property.
	 */
	DELAY_SHADING(22),

	/**
	 * The occlude property.
	 */
	OCCLUDE(23),

	/**
	 * The animation property.
	 */
	ANIMATION(24),

	/**
	 * The decor displacement property.
	 */
	DECOR_DISPLACEMENT(28),

	/**
	 * The ambient lighting property.
	 */
	AMBIENT_LIGHTING(29),

	/**
	 * The light diffusion property.
	 */
	LIGHT_DIFFUSION(39),

	/**
	 * The colours property.
	 */
	COLOURS(40),

	/**
	 * The minimap function property.
	 */
	MINIMAP_FUNCTION(60),

	/**
	 * The inverted property.
	 */
	INVERTED(62),

	/**
	 * The cast shadow property.
	 */
	CAST_SHADOW(64),

	/**
	 * The scale x property.
	 */
	SCALE_X(65),

	/**
	 * The scale y property.
	 */
	SCALE_Y(66),

	/**
	 * The scale z property.
	 */
	SCALE_Z(67),

	/**
	 * The mapscene property.
	 */
	MAPSCENE(68),

	/**
	 * The surroundings property.
	 */
	SURROUNDINGS(69),

	/**
	 * The translation x property.
	 */
	TRANSLATION_X(70),

	/**
	 * The translation y property.
	 */
	TRANSLATION_Y(71),

	/**
	 * The translation z property.
	 */
	TRANSLATION_Z(72),

	/**
	 * The obstructive ground property.
	 */
	OBSTRUCTIVE_GROUND(73),

	/**
	 * The hollow property.
	 */
	HOLLOW(74),

	/**
	 * The supports items property, indicating whether or not the object allows items to be placed on it.
	 */
	SUPPORTS_ITEMS(75),

	/**
	 * The {@link MorphismSet} property.
	 */
	MORPHISM_SET(77);

	/**
	 * The opcode of the property.
	 */
	private final int opcode;

	/**
	 * Creates the ObjectProperty.
	 * 
	 * @param opcode The opcode.
	 */
	private ObjectProperty(int opcode) {
		this.opcode = opcode;
	}

	@Override
	public int getOpcode() {
		return opcode;
	}

}