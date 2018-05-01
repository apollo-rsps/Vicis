package rs.emulate.scene3d.material

open class Material {
    open val vertexShader: ShaderSource = ShaderSource.Classpath("/shaders/default.vert")
    open val fragmentShader: ShaderSource = ShaderSource.Classpath("/shaders/default.frag")
}
