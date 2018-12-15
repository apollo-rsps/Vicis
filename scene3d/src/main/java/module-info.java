module rs.emulate.scene3d {
	requires kotlin.stdlib;

	requires javafx.controls;
	requires javafx.graphics;

	requires transitive org.joml;

	requires org.lwjgl;
	requires org.lwjgl.glfw;
	requires org.lwjgl.opengl;

	exports rs.emulate.scene3d;
	exports rs.emulate.scene3d.backend;
	exports rs.emulate.scene3d.backend.opengl;
	exports rs.emulate.scene3d.backend.opengl.target;
	exports rs.emulate.scene3d.backend.opengl.target.javafx;

	exports rs.emulate.scene3d.buffer;
	exports rs.emulate.scene3d.material;
}
