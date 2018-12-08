package rs.emulate.legacy.frame

import rs.emulate.shared.util.copy
import rs.emulate.shared.util.getSmart
import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedShort
import java.nio.ByteBuffer

/**
 * @buffer The _decompressed_ [ByteBuffer] (i.e. has already been gunzipped).
 */
class FrameDecoder(private val buffer: ByteBuffer) {

    fun decode(): List<AnimationFrame> {
        val frameCount = buffer.getUnsignedShort()
        val trailer = buffer.apply { position(buffer.limit() - 4 * java.lang.Short.BYTES) }

        val metadataLength = trailer.getUnsignedShort()
        val attributesLength = trailer.getUnsignedShort()
        val transformLength = trailer.getUnsignedShort()
        val durationLength = trailer.getUnsignedShort()

        buffer.position(0)
        val frames = ArrayList<AnimationFrame>(frameCount)

        val metadata = buffer.copy().apply { position(java.lang.Short.BYTES) }
        val attributes = buffer.copy().apply { position(metadata.position() + metadataLength) }
        val transforms = buffer.copy().apply { position(attributes.position() + attributesLength) }
        val durations = buffer.copy().apply { position(transforms.position() + transformLength) }

        val baseBuffer = buffer.copy().apply { position(durations.position() + durationLength) }
        val base = FrameBaseDecoder(baseBuffer).decode()

        for (frameIndex in 0 until frameCount) {
            val id = metadata.getUnsignedShort()
            val duration = durations.getUnsignedByte()

            val transformCount = metadata.getUnsignedByte()
            var lastValid = -1
            var transformations = 0

            val indices = arrayOfNulls<Int>(TRANSFORMATION_BUFFER_CAPACITY)
            val transformX = arrayOfNulls<Int>(TRANSFORMATION_BUFFER_CAPACITY)
            val transformY = arrayOfNulls<Int>(TRANSFORMATION_BUFFER_CAPACITY)
            val transformZ = arrayOfNulls<Int>(TRANSFORMATION_BUFFER_CAPACITY)

            for (index in 0 until transformCount) {
                val attributes = attributes.getUnsignedByte()
                if (attributes <= 0) {
                    continue
                }

                if (base.types[index] != TransformationType.RECENTER) {
                    for (next in index - 1 downTo lastValid + 1) {
                        if (base.types[next] == TransformationType.RECENTER) {
                            indices[transformations] = next

                            transformX[transformations] = 0
                            transformY[transformations] = 0
                            transformZ[transformations] = 0

                            transformations++
                            break
                        }
                    }
                }

                indices[transformations] = index
                val default = if (base.types[index] === TransformationType.SCALE) DEFAULT_SCALE else 0

                transformX[transformations] = if (attributes and 0x1 != 0) transforms.getSmart() else default
                transformY[transformations] = if (attributes and 0x2 != 0) transforms.getSmart() else default
                transformZ[transformations] = if (attributes and 0x4 != 0) transforms.getSmart() else default

                lastValid = index
                transformations++
            }

            frames += AnimationFrame(
                id = id,
                base = base,
                baseIndices = indices.filterNotNull().toIntArray(),
                transformX = transformX.filterNotNull().toIntArray(),
                transformY = transformY.filterNotNull().toIntArray(),
                transformZ = transformZ.filterNotNull().toIntArray(),
                duration = duration
            )
        }

        return frames
    }

    companion object {
        const val DEFAULT_SCALE = 128
        const val FRAME_INDEX = 2
        const val TRANSFORMATION_BUFFER_CAPACITY = 500
    }

}
