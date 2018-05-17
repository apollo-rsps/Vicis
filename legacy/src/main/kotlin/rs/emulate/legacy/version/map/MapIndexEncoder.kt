package rs.emulate.legacy.version.map

import rs.emulate.shared.util.DataBuffer

/**
 * An encoder for a [MapIndex].
 */
class MapIndexEncoder(private val index: MapIndex) {

    /**
     * Encodes the [MapIndex] into a [DataBuffer].
     */
    fun encode(): DataBuffer {
        val buffer = DataBuffer.allocate((3 * java.lang.Short.BYTES + java.lang.Byte.BYTES) * index.size)

        index.areas.forEach { buffer.putShort(it) }
        index.maps.forEach { buffer.putShort(it) }
        index.objects.forEach { buffer.putShort(it) }
        index.members.forEach { buffer.putBoolean(it) }

        return buffer.flip()
    }

}
