package rs.emulate.legacy.config.`object`

import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.npc.MorphismSet

sealed class ObjectProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object PositionedModels : ObjectProperty<ModelSet>(1)
    object Name : ObjectProperty<String>(2)
    object Description : ObjectProperty<String>(3)
    object Models : ObjectProperty<ModelSet>(5)
    object Width : ObjectProperty<Int>(14)
    object Length : ObjectProperty<Int>(15)
    object Solid : ObjectProperty<Boolean>(16)
    object Impenetrable : ObjectProperty<Boolean>(17)
    object Interactive : ObjectProperty<Boolean>(18)
    object ContourGround : ObjectProperty<Boolean>(21)
    object DelayShading : ObjectProperty<Boolean>(22)
    object Occlude : ObjectProperty<Boolean>(23)
    object Animation : ObjectProperty<Int>(24)
    object DecorDisplacement : ObjectProperty<Int>(28)
    object AmbientLighting : ObjectProperty<Int>(29)
    object LightDiffusion : ObjectProperty<Int>(39)
    object Colours : ObjectProperty<Map<Int, Int>>(40)
    object MinimapFunction : ObjectProperty<Int>(60)
    object Inverted : ObjectProperty<Boolean>(62)
    object CastShadow : ObjectProperty<Boolean>(64)
    object ScaleX : ObjectProperty<Int>(65)
    object ScaleY : ObjectProperty<Int>(66)
    object ScaleZ : ObjectProperty<Int>(67)
    object Mapscene : ObjectProperty<Int>(68)
    object Surroundings : ObjectProperty<Int>(69)
    object TranslationX : ObjectProperty<Int>(70)
    object TranslationY : ObjectProperty<Int>(71)
    object TranslationZ : ObjectProperty<Int>(72)
    object ObstructiveGround : ObjectProperty<Boolean>(73)
    object Hollow : ObjectProperty<Boolean>(74)
    object SupportsItems : ObjectProperty<Int>(75)
    object Morphisms : ObjectProperty<MorphismSet>(77)
}
