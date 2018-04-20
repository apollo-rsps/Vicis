package rs.emulate.legacy.map

import rs.emulate.legacy.map.MapConstants.HEIGHT_MULTIPLICAND
import rs.emulate.legacy.map.MapConstants.LOWEST_CONTINUED_TYPE
import rs.emulate.legacy.map.MapConstants.MINIMUM_ATTRIBUTES_TYPE
import rs.emulate.legacy.map.MapConstants.MINIMUM_OVERLAY_TYPE
import rs.emulate.legacy.map.MapConstants.ORIENTATION_COUNT
import rs.emulate.legacy.map.MapConstants.PLANE_HEIGHT_DIFFERENCE
import rs.emulate.shared.util.DataBuffer
import java.util.ArrayList
import java.util.Arrays

/**
 * An encoder for [MapFile]s.
 *
 * @param map The [MapFile].
 */
class MapFileEncoder(private val map: MapFile) {

    /**
     * Encodes the [MapFile] into a [DataBuffer].
     */
    fun encode(): DataBuffer {
        val planes = map.planes
        val buffers = ArrayList<DataBuffer>(planes.size)
        var size = 0

        for (plane in planes) {
            val tiles = plane.tiles()
            val buffer = DataBuffer.allocate(tiles.size * 6 * java.lang.Byte.BYTES)

            tiles.map(::encodeTile).forEach { buffer.put(it) }
            size += buffer.position()

            buffers.add(buffer)
        }

        return buffers.fold(DataBuffer.allocate(size), DataBuffer::put)
    }

    /**
     * Encodes a single [Tile] into a [DataBuffer].
     */
    private fun encodeTile(tile: Tile): DataBuffer {
        val bytes = ByteArray(java.lang.Byte.BYTES * 6)
        var index = 0

        val overlay = tile.overlay
        val orientation = tile.overlayOrientation
        val type = tile.overlayType

        if (overlay != 0 || orientation != 0 || type != 0) {
            require(orientation in 0 until ORIENTATION_COUNT) { "Orientation must be [0, $ORIENTATION_COUNT)." }

            val value = type * ORIENTATION_COUNT + LOWEST_CONTINUED_TYPE

            bytes[index++] = (value + orientation).toByte()
            bytes[index++] = overlay.toByte()
        }

        val attributes = tile.attributes
        if (attributes != 0) {
            bytes[index++] = (attributes + MINIMUM_OVERLAY_TYPE).toByte()
        }

        val underlay = tile.underlay
        if (underlay != 0) {
            bytes[index++] = (underlay + MINIMUM_ATTRIBUTES_TYPE).toByte()
        }

        var height = tile.height
        val position = tile.position
        val calculated = TileUtils.calculateHeight(position.x, position.z) % PLANE_HEIGHT_DIFFERENCE

        if (height == calculated) {
            bytes[index++] = 0
        } else {
            bytes[index++] = 1

            height %= PLANE_HEIGHT_DIFFERENCE // TODO verify
            bytes[index++] = (height / HEIGHT_MULTIPLICAND).toByte()
        }

        return DataBuffer.wrap(Arrays.copyOf(bytes, index))
    }

}
