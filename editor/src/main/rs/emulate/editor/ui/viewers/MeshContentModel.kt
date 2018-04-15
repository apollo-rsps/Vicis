package rs.emulate.editor.ui.viewers

import com.jme3.math.Vector3f
import javafx.beans.value.ObservableValue
import rs.emulate.editor.resource.ResourceContentModel
import java.util.*

data class MeshContentModel(val vertices: Array<Vector3f>, val faces: IntArray) : ResourceContentModel {
    override fun properties(): List<ObservableValue<*>> = emptyList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MeshContentModel

        if (!Arrays.equals(vertices, other.vertices)) return false
        if (!Arrays.equals(faces, other.faces)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(vertices)
        result = 31 * result + Arrays.hashCode(faces)
        return result
    }
}