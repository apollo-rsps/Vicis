module rs.emulate.modern {
	requires transitive rs.emulate.util;
	requires kotlin.stdlib;

	exports rs.emulate.modern;
	exports rs.emulate.modern.compression;
	exports rs.emulate.modern.fs;
}
