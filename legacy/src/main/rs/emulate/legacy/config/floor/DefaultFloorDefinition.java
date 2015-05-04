package rs.emulate.legacy.config.floor;

import static rs.emulate.legacy.config.Properties.alwaysFalse;
import static rs.emulate.legacy.config.Properties.asciiString;
import static rs.emulate.legacy.config.Properties.unsignedByte;
import static rs.emulate.legacy.config.Properties.unsignedTribyte;
import static rs.emulate.legacy.config.floor.FloorProperty.COLOUR;
import static rs.emulate.legacy.config.floor.FloorProperty.MINIMAP_COLOUR;
import static rs.emulate.legacy.config.floor.FloorProperty.NAME;
import static rs.emulate.legacy.config.floor.FloorProperty.SHADOWED;
import static rs.emulate.legacy.config.floor.FloorProperty.TEXTURE;

import java.util.HashMap;
import java.util.Map;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.DefaultConfigDefinition;

/**
 * A {@link DefaultConfigDefinition} for {@link FloorDefinition}s.
 *
 * @author Major
 */
public class DefaultFloorDefinition extends DefaultConfigDefinition {

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>();

		properties.put(1, unsignedTribyte(COLOUR, 0));
		properties.put(2, unsignedByte(TEXTURE, 0));
		properties.put(5, alwaysFalse(SHADOWED, true));
		properties.put(6, asciiString(NAME, null));
		properties.put(7, unsignedTribyte(MINIMAP_COLOUR, 0));

		return properties;
	}

}