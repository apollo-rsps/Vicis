package rs.emulate.editor.legacy.npc

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import rs.emulate.editor.core.workbench.properties.ResourcePropertySheetItem
import rs.emulate.legacy.config.npc.NpcDefinition
import java.util.*

sealed class NpcPropertySheetItem(property: NpcProperty) : ResourcePropertySheetItem<NpcProperty>(property) {

    class Name(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Name) {
        private val observable by lazy { SimpleStringProperty(null, "name", definition.name) }

        override fun setValue(value: Any?) {
            definition.name = (value ?: "") as String
        }

        override fun getType(): Class<*> = String::class.java
        override fun getValue(): Any = definition.name
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class Description(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Description) {
        private val observable by lazy { SimpleStringProperty(null, "description", definition.description) }

        override fun setValue(value: Any?) {
            value as String?
            definition.description = if (value.isNullOrBlank()) null else value
        }

        override fun getType(): Class<*> = String::class.java
        override fun getValue(): Any? = definition.description
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class Size(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Size) {
        private val observable by lazy { SimpleIntegerProperty(null, "size", definition.size) }

        override fun setValue(value: Any?) {
            definition.size = (value ?: 1) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.size
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class StandingSequence(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.StandingSequence) {
        private val observable by lazy { SimpleIntegerProperty(null, "standing_sequence", definition.standingSequence) }

        override fun setValue(value: Any?) {
            definition.standingSequence = (value ?: 0) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.standingSequence
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class WalkingSequence(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.WalkingSequence) {
        private val observable by lazy { SimpleIntegerProperty(null, "walking_sequence", definition.walkingSequence) }

        override fun setValue(value: Any?) {
            definition.walkingSequence = (value ?: 0) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.walkingSequence
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class VisibleOnMinimap(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.VisibleOnMinimap) {
        private val observable by lazy { SimpleBooleanProperty(null, "visible_on_minimap", definition.visibleOnMinimap) }

        override fun setValue(value: Any?) {
            definition.visibleOnMinimap = value as Boolean
        }

        override fun getType(): Class<*> = Boolean::class.java
        override fun getValue(): Any? = definition.visibleOnMinimap
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class CombatLevel(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.CombatLevel) {
        private val observable by lazy { SimpleIntegerProperty(null, "combat_level", definition.combatLevel) }

        override fun setValue(value: Any?) {
            if (value == null) return
            definition.combatLevel = value as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.combatLevel
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class PlanarScale(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.PlanarScale) {
        private val observable by lazy { SimpleIntegerProperty(null, "planar_scale", definition.planarScale) }

        override fun setValue(value: Any?) {
            definition.planarScale = (value ?: 128) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.planarScale
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class VerticalScale(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.VerticalScale) {
        private val observable by lazy { SimpleIntegerProperty(null, "vertical_scale", definition.verticalScale) }

        override fun setValue(value: Any?) {
            definition.verticalScale = (value ?: 128) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.verticalScale
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class PriorityRender(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.PriorityRender) {
        private val observable by lazy { SimpleBooleanProperty(null, "priority_render", definition.priorityRender) }

        override fun setValue(value: Any?) {
            definition.priorityRender = value as Boolean
        }

        override fun getType(): Class<*> = Boolean::class.java
        override fun getValue(): Any? = definition.priorityRender
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class Brightness(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Brightness) {
        private val observable by lazy { SimpleIntegerProperty(null, "brightness", definition.brightness) }

        override fun setValue(value: Any?) {
            definition.brightness = (value ?: 0) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.brightness
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class Diffusion(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Diffusion) {
        private val observable by lazy { SimpleIntegerProperty(null, "diffusion", definition.diffusion) }

        override fun setValue(value: Any?) {
            definition.diffusion = (value ?: 0) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.diffusion
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class HeadIcon(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.HeadIcon) {
        private val observable by lazy { SimpleIntegerProperty(null, "head_icon", definition.headIconId) }

        override fun setValue(value: Any?) {
            definition.headIconId = (value ?: -1) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.headIconId
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class DefaultOrientation(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.DefaultOrientation) {
        private val observable by lazy { SimpleIntegerProperty(null, "default_orientation", definition.defaultOrientation) }

        override fun setValue(value: Any?) {
            definition.defaultOrientation = (value ?: 32) as Int
        }

        override fun getType(): Class<*> = Int::class.java
        override fun getValue(): Any? = definition.defaultOrientation
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    class Clickable(private val definition: NpcDefinition) : NpcPropertySheetItem(NpcProperty.Clickable) {
        private val observable by lazy { SimpleBooleanProperty(null, "clickable", definition.clickable) }

        override fun setValue(value: Any?) {
            definition.clickable = value as Boolean
        }

        override fun getType(): Class<*> = Boolean::class.java
        override fun getValue(): Any? = definition.clickable
        override fun getObservableValue(): Optional<ObservableValue<out Any>> = Optional.of(observable)
    }

    companion object {
        fun from(definition: NpcDefinition): List<NpcPropertySheetItem> {
            return listOf(
                Name(definition),
                Description(definition),
                Size(definition),
                StandingSequence(definition),
                WalkingSequence(definition),
                VisibleOnMinimap(definition),
                CombatLevel(definition),
                PlanarScale(definition),
                VerticalScale(definition),
                PriorityRender(definition),
                Brightness(definition),
                Diffusion(definition),
                HeadIcon(definition),
                DefaultOrientation(definition),
                Clickable(definition)
            )
        }
    }

}

