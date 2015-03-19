package rs.emulate.shared.util.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import rs.emulate.shared.util.DataBuffer;

/**
 * An implementation of the RSA algorithm.
 *
 * @author Graham
 */
public final class Rsa {

	/**
	 * Encrypts/decrypts the specified {@link ByteBuffer} with the key and modulus.
	 *
	 * @param buffer The input buffer.
	 * @param modulus The modulus.
	 * @param key The key.
	 * @return The output buffer.
	 */
	public static DataBuffer crypt(DataBuffer buffer, BigInteger modulus, BigInteger key) {
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);

		BigInteger in = new BigInteger(bytes);
		BigInteger out = in.modPow(key, modulus);

		return DataBuffer.wrap(out.toByteArray());
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private Rsa() {

	}

}