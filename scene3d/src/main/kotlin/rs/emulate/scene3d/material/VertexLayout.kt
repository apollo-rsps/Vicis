package rs.emulate.scene3d.material

/**
 * An element in a [VertexLayout], defines the vertex attribute and whether it's required.
 */
data class VertexLayoutElement(val type: VertexAttribute<*>, val optional: Boolean = false)

/**
 * A vertex layout representation.  Defines the location and/or names of attributes used in a [Material]s
 * shader and their respective [VertexAttribute] types.
 */
data class VertexLayout(val elements: List<VertexLayoutElement>) {
    class Builder(val elements: MutableList<VertexLayoutElement> = mutableListOf<VertexLayoutElement>()) {
        operator fun VertexAttribute<*>.invoke(optional: Boolean = false) = elements.add(VertexLayoutElement(this, optional))
    }

    companion object {
        /**
         * Type-safe builder to easily construct a [VertexLayout] from [VertexAttribute]s.
         */
        fun vertexLayout(build: Builder.() -> Unit): VertexLayout {
            val bindings = with(Builder()) {
                build()
                elements
            }

            return VertexLayout(bindings)
        }
    }
}

