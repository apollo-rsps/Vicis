package rs.emulate.editor.core.workbench.properties.editors

import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableNumberValue
import javafx.scene.control.TextField
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor

class IntEditor(item: PropertySheet.Item) : NumericEditor<Int>(item, IntField())
class LongEditor(item: PropertySheet.Item) : NumericEditor<Long>(item, LongField())
class DoubleEditor(item: PropertySheet.Item) : NumericEditor<Double>(item, DoubleField())

abstract class NumericEditor<T : Number>(
    item: PropertySheet.Item,
    field: NumericField<T, *>
) : AbstractPropertyEditor<T, NumericField<T, *>>(item, field) {

    init {
        enableAutoSelectAll(editor)
    }

    override fun getObservableValue(): ObservableValue<T> {
        return editor.valueProperty()
    }

    override fun setValue(value: T) {
        editor.setText(value.toString())
    }
}

class IntField : NumericField<Int, SimpleIntegerProperty>(IntValidator, ::SimpleIntegerProperty)
class LongField : NumericField<Long, SimpleLongProperty>(LongValidator, ::SimpleLongProperty)
class DoubleField : NumericField<Double, SimpleDoubleProperty>(DoubleValidator, ::SimpleDoubleProperty)

abstract class NumericField<T : Number, V>(
    private val parser: NumberParser<T>,
    propertyFactory: (Any, String, T) -> V
) : TextField() where V : WritableNumberValue, V : ObservableValue<Number> {
    private val observable: V = propertyFactory(this, "value", parser.default)

    init {
        textProperty().addListener(InvalidationListener { observable.value = parser.parse(text) })
    }

    fun valueProperty(): ObservableValue<T> {
        @Suppress("UNCHECKED_CAST")
        return observable as ObservableValue<T>
    }

    override fun replaceText(start: Int, end: Int, text: String) {
        if (replaceValid(start, end, text)) {
            super.replaceText(start, end, text)
        }
    }

    override fun replaceSelection(text: String) {
        if (replaceValid(selection.start, selection.end, text)) {
            super.replaceSelection(text)
        }
    }

    private fun replaceValid(start: Int, end: Int, fragment: String): Boolean {
        val newText = text.substring(0, start) + fragment + text.substring(end)
        if (newText.isEmpty()) {
            return true
        }

        return parser.parse(newText) != null
    }
}

internal object IntValidator : NumberParser<Int>(0, String::toIntOrNull)
internal object LongValidator : NumberParser<Long>(0L, String::toLongOrNull)
internal object DoubleValidator : NumberParser<Double>(0.0, String::toDoubleOrNull)

abstract class NumberParser<T : Number>(
    val default: T,
    private val parser: String.() -> T?
) {
    fun parse(input: String?): T? {
        val trimmed = input?.trim() ?: return default
        return if (trimmed.isEmpty()) default else trimmed.parser()
    }
}
