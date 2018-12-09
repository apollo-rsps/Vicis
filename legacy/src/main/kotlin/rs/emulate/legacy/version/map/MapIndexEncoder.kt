package rs.emulate.legacy.version.map

import rs.emulate.util.putBoolean
import java.nio.ByteBuffer

/**
 * An encoder for a [MapIndex].
 */
class MapIndexEncoder(private val index: MapIndex) {

    /**
     * Encodes the [MapIndex] into a [ByteBuffer].
     */
    fun encode(): ByteBuffer {
        val buffer = ByteBuffer.allocate((3 * java.lang.Short.BYTES + java.lang.Byte.BYTES) * index.size)

        index.areas.forEach { buffer.putShort(it.toShort()) }
        index.maps.forEach { buffer.putShort(it.toShort()) }
        index.objects.forEach { buffer.putShort(it.toShort()) }
        index.members.forEach { buffer.putBoolean(it) }

        return buffer.apply { flip() }
    }

}
