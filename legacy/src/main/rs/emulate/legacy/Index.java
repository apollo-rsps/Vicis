package rs.emulate.legacy;

/**
 * An {@link Index} points to a file in the {@code main_file_cache.dat} file.
 *
 * @author Graham
 */
public final class Index {

	/**
	 * The amount of bytes required to store an index.
	 */
	public static final int BYTES = 6;

	/**
	 * The first block of the file.
	 */
	private final int block;

	/**
	 * The size of the file.
	 */
	private final int size;

	/**
	 * Creates the index.
	 *
	 * @param size The size of the file.
	 * @param block The first block of the file.
	 */
	public Index(int size, int block) { // TODO verify each fits in 24 bits
		this.size = size;
		this.block = block;
	}

	/**
	 * Gets the first block of the file.
	 *
	 * @return The first block of the file.
	 */
	public int getBlock() {
		return block;
	}

	/**
	 * Gets the size of the file.
	 *
	 * @return The size of the file.
	 */
	public int getSize() {
		return size;
	}

}