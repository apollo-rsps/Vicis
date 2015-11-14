package rs.emulate.legacy.map;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.world.Position;
import rs.emulate.util.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An encoder for {@link MapFile}s.
 *
 * @author Major
 */
public final class MapFileEncoder {

	/**
	 * The MapFile to encode.
	 */
	private final MapFile map;

	/**
	 * Creates the MapFileEncoder.
	 *
	 * @param map The {@link MapFile}.
	 */
	public MapFileEncoder(MapFile map) {
		this.map = map;
	}

	/**
	 * Encodes the {@link MapFile} into a {@link DataBuffer}.
	 *
	 * @return The MapFile.
	 */
	public DataBuffer encode() {
		MapPlane[] planes = map.getPlanes();
		List<DataBuffer> buffers = new ArrayList<>(planes.length);
		int size = 0;

		for (MapPlane plane : planes) {
			List<Tile> tiles = plane.getTiles().collect(Collectors.toList());
			DataBuffer buffer = DataBuffer.allocate(tiles.size() * 6 * Byte.BYTES);

			tiles.stream().map(this::encodeTile).forEach(buffer::put);
			size += buffer.position();

			buffers.add(buffer);
		}

		return buffers.stream().reduce(DataBuffer.allocate(size), DataBuffer::put);
	}

	/**
	 * Encodes a single {@link Tile} into a {@link DataBuffer}.
	 *
	 * @param tile The Tile to encode.
	 * @return The DataBuffer.
	 */
	private DataBuffer encodeTile(Tile tile) {
		byte[] bytes = new byte[Byte.BYTES * 6];
		int index = 0;

		int overlay = tile.getOverlay(), orientation = tile.getOverlayOrientation(), type = tile.getOverlayType();
		if (overlay != 0 || orientation != 0 || type != 0) {
			int value = type * MapConstants.ORIENTATION_COUNT + MapConstants.LOWEST_CONTINUED_TYPE;

			Assertions.checkWithin(0, MapConstants.ORIENTATION_COUNT - 1, orientation, "Orientation must be [0, "
					+ MapConstants.ORIENTATION_COUNT + ").");

			bytes[index++] = (byte) (value + orientation);
			bytes[index++] = (byte) overlay;
		}

		int attributes = tile.getAttributes();
		if (attributes != 0) {
			bytes[index++] = (byte) (attributes + MapConstants.MINIMUM_OVERLAY_TYPE);
		}

		int underlay = tile.getUnderlay();
		if (underlay != 0) {
			bytes[index++] = (byte) (underlay + MapConstants.MINIMUM_ATTRIBUTES_TYPE);
		}

		int height = tile.getHeight();
		Position position = tile.getPosition();
		int calculated = TileUtils.calculateHeight(position.getX(), position.getZ())
				% MapConstants.PLANE_HEIGHT_DIFFERENCE;

		if (height == calculated) {
			bytes[index++] = 0;
		} else {
			bytes[index++] = 1;

			height %= MapConstants.PLANE_HEIGHT_DIFFERENCE; // TODO verify
			bytes[index++] = (byte) (height / MapConstants.HEIGHT_MULTIPLICAND);
		}

		return DataBuffer.wrap(Arrays.copyOf(bytes, index));
	}

}