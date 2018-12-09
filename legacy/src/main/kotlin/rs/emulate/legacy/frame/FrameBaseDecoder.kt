package rs.emulate.legacy.frame

import rs.emulate.util.getUnsignedByte
import java.nio.ByteBuffer

class FrameBaseDecoder(private val buffer: ByteBuffer) {

    fun decode(): FrameBase {
        val count = buffer.getUnsignedByte()
        val types = arrayOfNulls<TransformationType>(count)
        val boneLists = arrayOfNulls<BoneList>(count)

        for (index in 0 until count) {
            types[index] = TransformationType.lookup(buffer.getUnsignedByte())
        }

        for (group in 0 until count) {
            val groupSize = buffer.getUnsignedByte()
            val bones = IntArray(groupSize)

            for (index in 0 until groupSize) {
                bones[index] = buffer.getUnsignedByte()
            }

            boneLists[group] = BoneList(bones)
        }

        return FrameBase(types.requireNoNulls(), boneLists.requireNoNulls())
    }

}
