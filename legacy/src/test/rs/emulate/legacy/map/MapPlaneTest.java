package rs.emulate.legacy.map;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import rs.emulate.shared.world.Position;

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

		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
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

				assertTrue("X coordinates are not equal (expected=" + x + ", actual=" + actualX + ").", x == actualX);
				assertTrue("Z coordinates are not equal (expected=" + z + ", actual=" + actualZ + ").", z == actualZ);
			}
		}
	}

}