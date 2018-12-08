package rs.emulate.editor.resource.bundles.legacy.config.sequence

sealed class SequenceProperty {
    object Frames : SequenceProperty()
    object LoopOffset : SequenceProperty()
    object InterleaveOrder : SequenceProperty()
    object Stretches : SequenceProperty()
    object Priority : SequenceProperty()
    object CharacterMainhand : SequenceProperty()
    object CharacterOffhand : SequenceProperty()
    object MaximumLoops : SequenceProperty()
    object AnimatingPrecedence : SequenceProperty()
    object WalkingPrecedence : SequenceProperty()
    object ReplayMode : SequenceProperty()
}
