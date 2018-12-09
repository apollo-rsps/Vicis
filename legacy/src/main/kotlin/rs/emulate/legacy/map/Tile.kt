package rs.emulate.legacy.map

import rs.emulate.util.world.Position

/**
 * A single tile on the map.
 *
 * @param position The [Position].
 * @param attributes The attributes.
 * @param height The height.
 * @param overlay The overlay id.
 * @param overlayType The overlay type.
 * @param overlayOrientation The overlay orientation.
 * @param underlay The underlay id.
 */
class Tile(
    val position: Position,
    val attributes: Int,
    val height: Int,
    val overlay: Int,
    val overlayType: Int,
    val overlayOrientation: Int,
    val underlay: Int
) {

    /**
     * A builder for a Tile.
     */
    class Builder(private var position: Position) {
        var attributes: Int = 0

        var height: Int = 0
        var overlay: Int = 0

        var overlayOrientation: Int = 0

        var overlayType: Int = 0

        var underlay: Int = 0

        fun build(): Tile {
            return Tile(position, attributes, height, overlay, overlayType, overlayOrientation, underlay)
        }

    }

}
