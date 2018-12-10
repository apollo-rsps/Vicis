package rs.emulate.scene3d

import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3i
import rs.emulate.scene3d.material.VertexAttribute
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Specification for a [Node] that has vertex data associated with it.
 */
abstract class Geometry : Node() {

    /**
     * The type of geometry the vertex data is rendered as.
     */
    abstract var geometryType: GeometryType

    /**
     * Geometry is dirty when it's initialized so the renderer knows to configure its state first.
     */
    override var dirty: Boolean = true

    /**
     * The collection of vertex attribute data that makes up this geometry.
     */
    var data: GeometryData = GeometryData()

    /**
     * The vertex positions of this [Geometry].
     */
    var positions: List<Vector3f> by attribute(VertexAttribute.Position)

    /**
     * The vertex normals of this [Geometry].
     */
    var normals: List<Vector3f> by attribute(VertexAttribute.Normal)

    /**
     * The vertex colors of this [Geometry].
     */
    var colors: List<Vector3f> by attribute(VertexAttribute.Color)

    /**
     * The texture coordinates of this [Geometry].
     */
    var texCoords: List<Vector2f> by attribute(VertexAttribute.TexCoord)

    /**
     * Optional list of vertex indices for drawing indexed geometry.  May be empty.
     */
    var indices: List<Vector3i> by attribute(VertexAttribute.Index)

    /**
     * Get a list of the data for the given vertex [attribute].
     */
    fun <C : List<V>, V : Any> get(attribute: VertexAttribute<V>): C = data.buffer(attribute).data as C

    /**
     * Serialize the collection of vertex [items] for the vertex [attribute] to the respective backing buffer.
     */
    fun <T : Any> setAll(attribute: VertexAttribute<T>, items: Collection<T>) {
        data.buffer(attribute).setAll(items)
        dirty = true
    }

    /**
     * Serialize the single [datum] for the vertex [attribute] to the respective backing buffer.
     */
    fun <T : Any> set(attribute: VertexAttribute<T>, datum: T) {
        data.buffer(attribute).set(datum)
        dirty = true
    }

    /**
     * Attribute delegate factory that provides accessors for lists of object values over [GeometryBuffer]s.
     *
     * @param C The type of the vertex attribute data container.
     * @param V The type of the vertex attribute data values.
     */
    protected fun <C : List<V>, V : Any> attribute(attribute: VertexAttribute<V>): ReadWriteProperty<Geometry, C> {
        return AttributeDelegate(attribute)
    }

    /**
     * A property delegate for [VertexAttribute]s that are backed by a list of elements.
     */
    protected class AttributeDelegate<T : Any, L : List<T>>(val attribute: VertexAttribute<T>) : ReadWriteProperty<Geometry, L> {
        override fun getValue(thisRef: Geometry, property: KProperty<*>): L = thisRef.get(attribute)

        override fun setValue(thisRef: Geometry, property: KProperty<*>, value: L) = thisRef.setAll(attribute, value)
    }
}
