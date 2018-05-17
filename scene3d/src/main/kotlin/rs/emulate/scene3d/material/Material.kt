package rs.emulate.scene3d.material

import rs.emulate.scene3d.material.VertexLayout.Companion.vertexLayout

open class Material {
    open val vertexShader: ShaderSource = ShaderSource.Classpath("/shaders/default.vert")
    open val fragmentShader: ShaderSource = ShaderSource.Classpath("/shaders/default.frag")

    open val vertexLayout = vertexLayout {
        VertexAttribute.Color(optional = true)
        VertexAttribute.Position(optional = false)
        VertexAttribute.Normal(optional = true)
    }
}
