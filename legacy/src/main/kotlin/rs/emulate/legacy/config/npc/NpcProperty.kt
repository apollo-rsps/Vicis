package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigPropertyType

sealed class NpcProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Models : NpcProperty<IntArray>(1)
    object Name : NpcProperty<String>(2)
    object Description : NpcProperty<String>(3)
    object Size : NpcProperty<Int>(12)
    object IdleAnimation : NpcProperty<Int>(13)
    object WalkingAnimation : NpcProperty<Int>(14)
    object AnimationSet : NpcProperty<MovementAnimationSet>(15)
    object Colours : NpcProperty<Map<Int, Int>>(40)
    object SecondaryModels : NpcProperty<IntArray>(60)
    object MinimapVisible : NpcProperty<Boolean>(93)
    object CombatLevel : NpcProperty<Int>(95)
    object FlatScale : NpcProperty<Int>(97)
    object HeightScale : NpcProperty<Int>(98)
    object PriorityRender : NpcProperty<Boolean>(99)
    object LightModifier : NpcProperty<Int>(100)
    object ShadowModifier : NpcProperty<Int>(101)
    object HeadIcon : NpcProperty<Int>(102)
    object Rotation : NpcProperty<Int>(103)
    object Morphisms : NpcProperty<MorphismSet>(106)
    object Clickable : NpcProperty<Boolean>(107)
}
