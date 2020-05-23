package rs.emulate.common.map

import io.netty.buffer.ByteBuf
import rs.emulate.common.CacheItemDecoder
import rs.emulate.util.readUnsignedMultiSmart
import rs.emulate.util.readUnsignedSmart

object MapLocationsDecoder : CacheItemDecoder<MapId, MapLocations> {
    override fun decode(id: MapId, input: ByteBuf): MapLocations {
        val locations = mutableListOf<MapLocation>()

        var objectId = -1
        var idOffset: Int = input.readUnsignedMultiSmart()

        while (idOffset != 0) {
            objectId += idOffset

            var packed = 0
            var positionOffset = input.readUnsignedSmart()

            while (positionOffset != 0) {
                packed += positionOffset - 1

                val attributes = input.readUnsignedByte().toInt()
                val type = attributes shr 2
                val orientation = attributes and 0x3
                locations.add(MapLocation(objectId, packed, type, orientation))

                positionOffset = input.readUnsignedSmart()
            }

            idOffset = input.readUnsignedMultiSmart()
        }

        return MapLocations(id, locations)
    }
}
