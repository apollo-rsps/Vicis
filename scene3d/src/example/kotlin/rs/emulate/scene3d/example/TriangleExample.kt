package rs.emulate.scene3d.example

import glm_.vec3.Vec3
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import rs.emulate.scene3d.Mesh
import rs.emulate.scene3d.Scene
import rs.emulate.scene3d.backend.opengl.OpenGLRenderer
import rs.emulate.scene3d.backend.opengl.target.OpenGLDefaultRenderTarget

fun main(args: Array<String>) {
    val scene = Scene()
    val triangle = Mesh()

    scene.width = 800
    scene.height = 600
    scene.camera.move(0f, 0f, -5f)
    scene.camera.target.put(Vec3(0f, 1f, 0f))
    scene.camera.perspective(800, 600, 45f, 0.01f, 100f)
    scene.addChild(triangle)

    triangle.positions = listOf(
        Vec3(1f, 0f, 0f),
        Vec3(0f, 2f, 8f),
        Vec3(-1f, 0f, 0f)
    )

    val renderer = OpenGLRenderer(scene, OpenGLDefaultRenderTarget())
    renderer.initialize()

    while(!glfwWindowShouldClose(renderer.windowContext)) {
        if (renderer.initialized) {
            scene.update()
            renderer.render()
        }

        glfwPollEvents()
    }
}
