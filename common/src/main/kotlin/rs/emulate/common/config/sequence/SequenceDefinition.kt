package rs.emulate.common.config.sequence

import rs.emulate.common.config.Definition


class SequenceDefinition(
    override val id: Int,
    var frameCollection: FrameCollection = FrameCollection.EMPTY,
    var loopOffset: Int = -1,
    var interleaveOrder: IntArray? = null, // TODO
    var stretches: Boolean = false,
    var priority: Int = 5,
    var characterMainhand: Int = -1,
    var characterOffhand: Int = -1,
    var maximumLoops: Int = 99,
    var animatingPrecedence: Int = if (interleaveOrder == null) 0 else 2,
    var walkingPrecedence: Int = if (interleaveOrder == null) 0 else 2,
    var replayMode: Int = 2
) : Definition
