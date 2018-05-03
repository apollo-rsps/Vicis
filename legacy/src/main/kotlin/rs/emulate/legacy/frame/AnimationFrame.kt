package rs.emulate.legacy.frame

import java.util.Arrays

/**
 * A single keyframe of animation.
 * @param baseIndices The pointers into the lists in in the [FrameBase] (i.e. the [TransformationType] of transformation
 * 42 in this frame is `base.types[baseIndices[42]]`).
 */
class AnimationFrame(
    val id: Int,
    val base: FrameBase,
    val baseIndices: IntArray,
    val transformX: IntArray,
    val transformY: IntArray,
    val transformZ: IntArray,
    var duration: Int = 0 // TODO all 0-duration frames should be remapped to a duration of 1
)

/**
 * A base for an [AnimationFrame].
 */
class FrameBase( // TODO alternative name?
    val types: Array<TransformationType>,
    val bones: Array<BoneList>
)

/**
 * A list of bones to apply a single transformation to.
 */
data class BoneList(private val boneIndices: IntArray) {
    override fun equals(other: Any?): Boolean {
        return other is BoneList && Arrays.equals(boneIndices, other.boneIndices)
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(boneIndices)
    }
}
