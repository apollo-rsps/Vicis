open module rs.emulate.editor {
	requires transitive rs.emulate.legacy;
	requires transitive rs.emulate.modern;
	requires transitive rs.emulate.util;
	requires transitive rs.emulate.scene3d;

	requires kotlin.stdlib;
	requires kotlin.reflect;
	requires kotlinx.coroutines.core;

	requires java.logging;
	requires java.desktop;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;

	// Modules that provide UI components or extensions.
	requires controlsfx;
	requires dockfx;
	requires tiwulfx;

	requires javax.inject;
	requires com.google.guice;
	requires com.google.guice.extensions.assistedinject;
	requires com.google.guice.extensions.multibindings;

	requires io.netty.buffer;
	requires io.github.classgraph;
	requires com.dooapp.fxform;
	requires kotlinx.coroutines.javafx;

	requires org.kordamp.iconli.core;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.foundation;
	requires kotlinx.coroutines.jdk8;

	// Only export our main class.
	exports rs.emulate.editor;
}
