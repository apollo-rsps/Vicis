package rs.emulate.editor.utils.javafx

import javafx.collections.ListChangeListener.Change
import javafx.collections.ObservableList
import javafx.collections.transformation.TransformationList
import java.util.*


class MappedList<E, F>(private val mapper: (F) -> E, source: ObservableList<F>) : TransformationList<E, F>(source) {

    /**
     * An internal collection of source elements mapped to the target type.
     */
    private val mapped: ArrayList<E> = ArrayList<E>(source.map(mapper))

    override val size: Int
        get() = mapped.size

    override fun get(index: Int) = mapped[index]

    override fun getViewIndex(index: Int) = index

    override fun getSourceIndex(index: Int) = index

    override fun sourceChanged(change: Change<out F>) {
        beginChange()

        while (change.next()) {
            if (change.wasPermutated()) {
                val permutations = IntArray(change.to - change.from)

                for (idx in change.from until change.to) {
                    permutations[idx - change.from] = change.getPermutation(idx)
                }

                nextPermutation(change.from, change.to, permutations)
            } else if (change.wasUpdated()) {
                for (index in change.from until change.to) {
                    nextUpdate(index)
                }
            } else {
                if (change.wasRemoved()) {
                    val removed = mapped.subList(change.from, change.from + change.removedSize)
                    val duped = ArrayList(removed)

                    removed.clear()
                    nextRemove(change.from, duped)
                }

                if (change.wasAdded()) {
                    mapped.addAll(change.from, change.addedSubList.map(mapper))
                    nextAdd(change.from, change.to)
                }
            }
        }

        endChange()
    }
}
