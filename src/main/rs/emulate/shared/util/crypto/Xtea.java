package rs.emulate.shared.util.crypto;

import rs.emulate.shared.util.DataBuffer;

/**
 * An implementation of the XTEA block cipher.
 *
 * @author Graham
 */
public final class Xtea {

	/**
	 * The golden ratio.
	 */
	public static final int GOLDEN_RATIO = 0x9E3779B9;

	/**
	 * The number of rounds.
	 */
	public static final int ROUNDS = 32;

	/**
	 * Deciphers the data in the specified {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @param start The start value.
	 * @param end The end value.
	 * @param key The key.
	 * @throws IllegalArgumentException if the key is not exactly 4 elements long.
	 */
	public static void decipher(DataBuffer buffer, int start, int end, int[] key) {
		if (key.length != 4) {
			throw new IllegalArgumentException("Key length must be four.");
		}

		int numQuads = (end - start) / 8;
		for (int i = 0; i < numQuads; i++) {
			int sum = GOLDEN_RATIO * ROUNDS;
			int v0 = buffer.getInt(start + i * 8);
			int v1 = buffer.getInt(start + i * 8 + 4);
			for (int j = 0; j < ROUNDS; j++) {
				v1 -= (v0 << 4 ^ v0 >>> 5) + v0 ^ sum + key[sum >>> 11 & 3];
				sum -= GOLDEN_RATIO;
				v0 -= (v1 << 4 ^ v1 >>> 5) + v1 ^ sum + key[sum & 3];
			}
			buffer.putInt(start + i * 8, v0);
			buffer.putInt(start + i * 8 + 4, v1);
		}
	}

	/**
	 * Enciphers the specified {@link DataBuffer} with the given key.
	 *
	 * @param buffer The buffer.
	 * @param start The start value.
	 * @param end The end value.
	 * @param key The key.
	 * @throws IllegalArgumentException if the key is not exactly 4 elements long.
	 */
	public static void encipher(DataBuffer buffer, int start, int end, int[] key) {
		if (key.length != 4) {
			throw new IllegalArgumentException("Key length must be four.");
		}

		int numQuads = (end - start) / 8;
		for (int i = 0; i < numQuads; i++) {
			int sum = 0;
			int v0 = buffer.getInt(start + i * 8);
			int v1 = buffer.getInt(start + i * 8 + 4);
			for (int j = 0; j < ROUNDS; j++) {
				v0 += (v1 << 4 ^ v1 >>> 5) + v1 ^ sum + key[sum & 3];
				sum += GOLDEN_RATIO;
				v1 += (v0 << 4 ^ v0 >>> 5) + v0 ^ sum + key[sum >>> 11 & 3];
			}
			buffer.putInt(start + i * 8, v0);
			buffer.putInt(start + i * 8 + 4, v1);
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private Xtea() {

	}

}