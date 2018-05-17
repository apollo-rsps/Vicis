package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.ConfigPropertyType

sealed class AnimationProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Frames : AnimationProperty<FrameCollection>(1)
    object LoopOffset : AnimationProperty<Int>(2)
    object InterleaveOrder : AnimationProperty<ByteArray>(3)
    object Stretches : AnimationProperty<Boolean>(4)
    object Priority : AnimationProperty<Int>(5)
    object CharacterMainhand : AnimationProperty<Int>(6)
    object CharacterOffhand : AnimationProperty<Int>(7)
    object MaximumLoops : AnimationProperty<Int>(8)
    object AnimatingPrecedence : AnimationProperty<Int>(9)
    object WalkingPrecedence : AnimationProperty<Int>(10)
    object ReplayMode : AnimationProperty<Int>(11)
}
