package rs.emulate.legacy.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Identifies the actual name of an ArchiveEntry identifier.
 * 
 * @author Major
 */
final class NameIdentifier {

	private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	/**
	 * The Executor serving as a thread pool.
	 */
	private final Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

	public static void main(String[] args) {
		NameIdentifier identifier = new NameIdentifier(22834782);// , -1857300557);
		identifier.start();
	}

	/**
	 * The identifiers to find the names of.
	 */
	private final int[] identifiers;

	/**
	 * Creates the NameIdentifier.
	 *
	 * @param identifiers The identifiers to find the names of.
	 */
	private NameIdentifier(int... identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * Starts the name identification process.
	 */
	private void start() {
		for (int identifier : identifiers) {
			BruteForceIdentifier force = new BruteForceIdentifier(identifier);
			executor.execute(force);
		}
	}

	/**
	 * Identifies the names of ArchiveEntries using a brute-force search.
	 */
	private static final class BruteForceIdentifier implements Runnable {

		/**
		 * The identifier to find the name of.
		 */
		private final int identifier;

		/**
		 * Creates the BruteForceIdentifier.
		 *
		 * @param identifier The identifier to find the name of.
		 */
		public BruteForceIdentifier(int identifier) {
			this.identifier = identifier;
		}

		@Override
		public void run() {
			String match = recurse(1);
		}

		public String recurse(int length) {
			System.out.println("length=" + length);
			
			int offset = ".dat".length();
			char[] chars = new char[length + offset];
			
			int max = IDENTIFIER_CHARS.length;
			char[] table = IDENTIFIER_CHARS;
			int identifier = this.identifier;

			for (int i = 0; i < length; i++) {
				for (int char_index = 0; char_index < length; char_index++) {
					for (int table_index = 0; table_index < max; table_index++) {
						chars[char_index] = table[table_index];

						chars[char_index + 1] = '.';
						chars[char_index + 2] = 'd';
						chars[char_index + 3] = 'a';
						chars[char_index + 4] = 't';
						System.out.println(new String(chars));

						if (hash(chars) == identifier) {
							return new String(chars);
						}
					}
				}
			}
			if (length > 3) {
				throw new RuntimeException("length > 20");
			}

			return recurse(length + 1);
		}

	}

	public static int hash(char[] chars) {
		int hash = 0;
		int length = chars.length;

		for (int index = 0; index < length; index++) {
			hash = hash * 61 + chars[index] - 32;
		}

		return hash;
	}

	public static int hash(String name) {
		int hash = 0;
		int length = name.length();

		for (int index = 0; index < length; index++) {
			hash = hash * 61 + name.charAt(index) - 32;
		}

		return hash;
	}

	/**
	 * The characters used in an identifier.
	 */
	private static final char[] IDENTIFIER_CHARS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };

}