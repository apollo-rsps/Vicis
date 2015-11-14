package rs.emulate.legacy.map;

import org.junit.Test;
import rs.emulate.shared.world.Position;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MapPlane} class.
 *
 * @author Major
 */
public final class MapPlaneTest {

	/**
	 * Tests the {@link MapPlane#getTiles} method.
	 */
	@Test
	public void getTiles() {
		int count = 3;
		Tile[][] tiles = new Tile[count][count];

		for (int x = 0; x < count; x++) {
			for (int z = 0; z < count; z++) {
				tiles[x][z] = new Tile(new Position(x, z), 0, 0, 0, 0, 0, 0);
			}
		}

		MapPlane plane = new MapPlane(0, tiles);
		List<Tile> fetched = plane.getTiles().collect(Collectors.toList());
		int index = 0;

		for (int x = 0; x < count; x++) {
			for (int z = 0; z < count; z++) {
				Tile tile = fetched.get(index++);
				Position position = tile.getPosition();

				int actualX = position.getX();
				int actualZ = position.getZ();

				assertEquals(x, actualX);
				assertEquals(z, actualZ);
			}
		}
	}

}