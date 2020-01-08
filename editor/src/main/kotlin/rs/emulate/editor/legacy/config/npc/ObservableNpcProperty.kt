package rs.emulate.editor.legacy.config.npc

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import rs.emulate.editor.core.workbench.properties.ObservableProperty
import rs.emulate.editor.vfs.LegacyFileLoader
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.config.sequence.SequenceDefinitionDecoder

sealed class ObservableNpcProperty<T>(
    initialValue: T,
    property: NpcProperty
) : ObservableProperty<T>(property, initialValue) {

    abstract fun apply(definition: NpcDefinition): NpcDefinition

    class Models(
        models: List<Int>,
        private val loader: LegacyFileLoader
    ) : ObservableNpcProperty<List<Int>>(models, NpcProperty.Models) {
        override fun apply(definition: NpcDefinition) = definition.copy(models = delegate.value)
    }

    class Name(name: String) : ObservableNpcProperty<String>(name, NpcProperty.Name) {
        override fun apply(definition: NpcDefinition) = definition.copy(name = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: "null")
        }
    }

    class Description(description: String?) : ObservableNpcProperty<String?>(description, NpcProperty.Description) {
        override fun apply(definition: NpcDefinition) = definition.copy(description = delegate.value)
    }

    class Size(size: Int) : ObservableNpcProperty<Int>(size, NpcProperty.Size) {
        override fun apply(definition: NpcDefinition) = definition.copy(size = delegate.value)
    }

    class StandingSequence(
        sequence: Int,
        private val loader: LegacyFileLoader
    ) : ObservableNpcProperty<Int>(sequence, NpcProperty.StandingSequence) {
        override fun apply(definition: NpcDefinition) = definition.copy(standingSequence = delegate.value)

        override fun setValue(value: Any?): Boolean {
            val max = loader.configFileCount(SequenceDefinitionDecoder.entryName)

            val result = value.tryParseInt(default = -1, max = max)
                .onSuccess(delegate::set)
                .onFailure { /* TODO alert the user */ }

            return result is Ok
        }
    }

    class WalkingSequence(
        sequence: Int,
        private val loader: LegacyFileLoader
    ) : ObservableNpcProperty<Int>(sequence, NpcProperty.WalkingSequence) {
        override fun apply(definition: NpcDefinition) = definition.copy(walkingSequence = delegate.value)

        override fun setValue(value: Any?): Boolean {
            val max = loader.configFileCount(SequenceDefinitionDecoder.entryName)

            val result = value.tryParseInt(default = -1, max = max)
                .onSuccess(delegate::set)
                .onFailure { /* TODO alert the user */ }

            return result is Ok
        }
    }

    class VisibleOnMinimap(visible: Boolean) : ObservableNpcProperty<Boolean>(visible, NpcProperty.VisibleOnMinimap) {
        override fun apply(definition: NpcDefinition) = definition.copy(visibleOnMinimap = delegate.value)
    }

    class CombatLevel(level: Int) : ObservableNpcProperty<Int>(level, NpcProperty.CombatLevel) {
        override fun apply(definition: NpcDefinition) = definition.copy(combatLevel = delegate.value)
    }

    class PlanarScale(scale: Int) : ObservableNpcProperty<Int>(scale, NpcProperty.PlanarScale) {
        override fun apply(definition: NpcDefinition) = definition.copy(planarScale = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: 128)
        }
    }

    class VerticalScale(scale: Int) : ObservableNpcProperty<Int>(scale, NpcProperty.VerticalScale) {
        override fun apply(definition: NpcDefinition) = definition.copy(verticalScale = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: 128)
        }
    }

    class PriorityRender(priority: Boolean) : ObservableNpcProperty<Boolean>(priority, NpcProperty.PriorityRender) {
        override fun apply(definition: NpcDefinition) = definition.copy(priorityRender = delegate.value)
    }

    class Brightness(brightness: Int) : ObservableNpcProperty<Int>(brightness, NpcProperty.Brightness) {
        override fun apply(definition: NpcDefinition) = definition.copy(brightness = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: 0)
        }
    }

    class Diffusion(diffusion: Int) : ObservableNpcProperty<Int>(diffusion, NpcProperty.Diffusion) {
        override fun apply(definition: NpcDefinition) = definition.copy(diffusion = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: 0)
        }
    }

    class HeadIcon(
        id: Int,
        private val loader: LegacyFileLoader
    ) : ObservableNpcProperty<Int>(id, NpcProperty.HeadIcon) {
        override fun apply(definition: NpcDefinition) = definition.copy(headIconId = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: -1)
        }
    }

    class DefaultOrientation(default: Int) : ObservableNpcProperty<Int>(default, NpcProperty.DefaultOrientation) {
        override fun apply(definition: NpcDefinition) = definition.copy(defaultOrientation = delegate.value)

        override fun setValue(value: Any?): Boolean {
            return super.setValue(value ?: 32)
        }
    }

    class Clickable(visible: Boolean) : ObservableNpcProperty<Boolean>(visible, NpcProperty.Clickable) {
        override fun apply(definition: NpcDefinition) = definition.copy(clickable = delegate.value)
    }

}
