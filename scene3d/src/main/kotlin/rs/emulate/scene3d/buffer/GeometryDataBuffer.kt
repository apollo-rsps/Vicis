package rs.emulate.scene3d.buffer

import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.reflect.KClass

abstract class GeometryDataBuffer<T : Any>(val components: Int, val componentSize: Int) {
    /**
     * A backing buffer (must be direct since it's used by native backends) for geometry data.
     */
    var buffer = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());

    /**
     * A list of the data items that were last added to this buffer.
     */
    val data = mutableListOf<T>()

    /**
     * Check if the backing buffer currently has no data associated with it.
     */
    fun isEmpty(): Boolean {
        return buffer.limit() == 0
    }

    /**
     * Clear the current buffer and set its data to the supplied collection.
     */
    fun setAll(items: Collection<T>) {
        checkBufferExtents(items.size)
        buffer.clear()
        data.clear()

        data.addAll(items)
        for ((offset, datum) in items.withIndex()) {
            set(offset, datum)
        }
    }

    /**
     * Clear the buffer and insert the supplied datum as the first element.
     */
    fun set(datum: T) {
        checkBufferExtents(1)
        buffer.clear()
        data.clear()

        data.add(0, datum)
        set(0, datum)
    }

    /**
     * Put the [data] into the backing [buffer] at the given [offset]
     */
    protected abstract fun set(offset: Int, data: T)

    /**
     * Checks if the backing buffer has the capacity to hold [elements] and resizes it
     * to the required size if not.
     */
    internal fun checkBufferExtents(elements: Int) {
        val size = componentSize * components * elements

        if (buffer.capacity() < size) {
            buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
        }

        buffer.position(0)
        buffer.limit(size)
    }

    /**
     * Get the byte position of the given [elementOffset].
     */
    internal fun elementOffset(elementOffset: Int) = elementOffset * components * componentSize

    companion object {
        /**
         * Create a new data buffer for the given type.  Valid types are vertex and matrix types from GLM.
         */
        fun <T : Any> create(type: KClass<T>): GeometryDataBuffer<T> {
            val buffer = when (type) {
                Vec2::class -> Vec2fDataBuffer()
                Vec3::class -> Vec3fDataBuffer()
                Mat4::class -> Mat4fDataBuffer()
                else -> throw IllegalStateException("VertexAttribute: ${this::class.java.simpleName} has an invalid element type: ${type.java.simpleName}")
            }

            return buffer as GeometryDataBuffer<T>
        }
    }
}
