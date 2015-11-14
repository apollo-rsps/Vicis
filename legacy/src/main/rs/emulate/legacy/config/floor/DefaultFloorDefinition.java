package rs.emulate.legacy.config.floor;

import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

import java.util.HashMap;
import java.util.Map;

import static rs.emulate.legacy.config.Properties.alwaysFalse;
import static rs.emulate.legacy.config.Properties.asciiString;
import static rs.emulate.legacy.config.Properties.unsignedByte;
import static rs.emulate.legacy.config.Properties.unsignedTribyte;
import static rs.emulate.legacy.config.floor.FloorProperty.*;

/**
 * A {@link DefaultConfigDefinition} for {@link FloorDefinition}s.
 *
 * @param <T> The type of {@link FloorDefinition} this default is for.
 * @author Major
 */
public class DefaultFloorDefinition<T extends FloorDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> properties = new HashMap<>();

		properties.put(1, unsignedTribyte(COLOUR, 0));
		properties.put(2, unsignedByte(TEXTURE, 0));
		properties.put(5, alwaysFalse(SHADOWED, true));
		properties.put(6, asciiString(NAME, null));
		properties.put(7, unsignedTribyte(MINIMAP_COLOUR, 0));

		return properties;
	}

}