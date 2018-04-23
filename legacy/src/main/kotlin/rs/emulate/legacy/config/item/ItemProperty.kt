package rs.emulate.legacy.config.item

import rs.emulate.legacy.config.ConfigPropertyType


sealed class ItemProperty<T>(override val opcode: Int) : ConfigPropertyType<T>() {
    object Model : ItemProperty<Int>(1)
    object Name : ItemProperty<String>(2)
    object Description : ItemProperty<String>(3)
    object SpriteScale : ItemProperty<Int>(4)
    object SpritePitch : ItemProperty<Int>(5)
    object SpriteCameraRoll : ItemProperty<Int>(6)
    object SpriteTranslateX : ItemProperty<Int>(7)
    object SpriteTranslateY : ItemProperty<Int>(8)
    object Stackable : ItemProperty<Boolean>(11)
    object Value : ItemProperty<Long>(12)
    object Members : ItemProperty<Boolean>(16)
    object PrimaryMaleModel : ItemProperty<PrimaryModel>(23)
    object SecondaryMaleModel : ItemProperty<Int>(24)
    object PrimaryFemaleModel : ItemProperty<PrimaryModel>(25)
    object SecondaryFemaleModel : ItemProperty<Int>(26)
    object Colours : ItemProperty<Map<Int, Int>>(40)
    object TertiaryMaleModel : ItemProperty<Int>(78)
    object TertiaryFemaleModel : ItemProperty<Int>(79)
    object PrimaryMaleHeadPiece : ItemProperty<Int>(90)
    object PrimaryFemaleHeadPiece : ItemProperty<Int>(91)
    object SecondaryMaleHeadPiece : ItemProperty<Int>(92)
    object SecondaryFemaleHeadPiece : ItemProperty<Int>(93)
    object SpriteCameraYaw : ItemProperty<Int>(95)
    object NoteInfoId : ItemProperty<Int>(97)
    object NoteTemplateId : ItemProperty<Int>(98)
    object GroundScaleX : ItemProperty<Int>(110)
    object GroundScaleY : ItemProperty<Int>(111)
    object GroundScaleZ : ItemProperty<Int>(112)
    object Ambience : ItemProperty<Int>(113)
    object Contrast : ItemProperty<Int>(114)
    object Team : ItemProperty<Int>(115)
} // TODO action and stack properties
