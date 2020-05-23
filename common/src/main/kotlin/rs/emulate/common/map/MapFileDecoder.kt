package rs.emulate.common.map

import io.netty.buffer.ByteBuf
import rs.emulate.common.CacheItemDecoder
import rs.emulate.common.map.MapConstants.HEIGHT_MULTIPLICAND
import rs.emulate.common.map.MapConstants.LOWEST_CONTINUED_TYPE
import rs.emulate.common.map.MapConstants.MINIMUM_ATTRIBUTES_TYPE
import rs.emulate.common.map.MapConstants.MINIMUM_OVERLAY_TYPE
import rs.emulate.common.map.MapConstants.ORIENTATION_COUNT
import rs.emulate.common.map.MapConstants.PLANE_HEIGHT_DIFFERENCE
import rs.emulate.common.map.Tile.Builder
import rs.emulate.util.world.Position

/**
 * A decoder for a [MapFile].
 *
 * @param buffer The ByteBuf containing the MapFile data. Should not be compressed.
 */
object MapFileDecoder : CacheItemDecoder<MapId, MapFile> {

    /**
     * Decodes the data into a [MapFile].
     */
    override fun decode(id: MapId, input: ByteBuf): MapFile {
        val planes = arrayOfNulls<MapPlane>(MapFile.PLANES)

        for (level in 0 until MapFile.PLANES) {
            planes[level] = decodePlane(input, planes, level)
        }

        return MapFile(id, planes.requireNoNulls())
    }

    /**
     * Decodes a [MapPlane] with the specified level.
     *
     * @param planes The previously-decoded [MapPlane]s, for calculating the height of the tiles.
     * @param level The height level.
     * @return The MapPlane.
     */
    private fun decodePlane(buf: ByteBuf, planes: Array<MapPlane?>, level: Int): MapPlane {
        val tiles = Array(MapFile.WIDTH) { x ->
            Array(MapFile.WIDTH) { z ->
                decodeTile(buf, planes, level, x, z)
            }
        }

        return MapPlane(level, tiles)
    }

    /**
     * Decodes the data into a [Tile].
     *
     * @param planes The previously-decoded [MapPlane]s, for calculating the height of the Tile.
     * @param level The height level the Tile is on.
     * @param x The x coordinate of the Tile.
     * @param z The z coordinate of the Tile.
     */
    private fun decodeTile(buffer: ByteBuf, planes: Array<MapPlane?>, level: Int, x: Int, z: Int): Tile {
        val builder = Builder(Position(x, z, level))

        var type: Int
        do {
            type = buffer.readUnsignedByte().toInt()

            if (type == 0) {
                if (level == 0) {
                    builder.height = TileUtils.calculateHeight(x, z)
                } else {
                    val below = planes[level - 1]!!.getTile(x, z)
                    builder.height = below.height + PLANE_HEIGHT_DIFFERENCE
                }
            } else if (type == 1) {
                val height = buffer.readUnsignedByte().toInt()
                val below = if (level == 0) 0 else planes[level - 1]!!.getTile(x, z).height

                builder.height = (if (height == 1) 0 else height) * HEIGHT_MULTIPLICAND + below
            } else if (type <= MINIMUM_OVERLAY_TYPE) {
                builder.overlay = buffer.readByte().toInt()
                builder.overlayType = (type - LOWEST_CONTINUED_TYPE) / ORIENTATION_COUNT
                builder.overlayOrientation = type - LOWEST_CONTINUED_TYPE % ORIENTATION_COUNT
            } else if (type <= MINIMUM_ATTRIBUTES_TYPE) {
                builder.attributes = type - MINIMUM_OVERLAY_TYPE
            } else {
                builder.underlay = type - MINIMUM_ATTRIBUTES_TYPE
            }
        } while (type >= LOWEST_CONTINUED_TYPE)

        return builder.build()
    }
}
