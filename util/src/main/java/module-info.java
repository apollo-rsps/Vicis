module rs.emulate.util {
	requires io.netty.buffer;
	requires io.netty.common;
	requires org.apache.commons.compress;
	requires org.bouncycastle.provider;

	exports rs.emulate.util;
	exports rs.emulate.util.channels;
	exports rs.emulate.util.charset;
	exports rs.emulate.util.compression;
	exports rs.emulate.util.crypto.digest;
	exports rs.emulate.util.crypto.rsa;
	exports rs.emulate.util.crypto.xtea;
	exports rs.emulate.util.cs;
	exports rs.emulate.util.world;
}
