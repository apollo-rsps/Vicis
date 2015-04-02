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
		NameIdentifier identifier = new NameIdentifier(8297314, 1654911043);
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
			int current = 0;
			while (true) {
				StringBuilder builder = new StringBuilder();

				int next = current;
				do {
					builder.append(IDENTIFIER_CHARS[Math.abs(next % 37)]);
					next /= 37;
				} while (next != 0);

				if (hash(builder.toString().concat(".dat")) == identifier
						|| hash(builder.toString().concat(".idx")) == identifier) {
					System.out.println("Found for " + identifier + ": " + builder.toString());
					break;
				}

				current++;
			}
		}
	}

	public static int hash(String name) {
		int hash = 0;
		for (char character : name.toUpperCase().toCharArray()) {
			hash = hash * 61 + character - 32;
		}

		return hash;
	}

	/**
	 * The characters used in an identifier.
	 */
	private static final char[] IDENTIFIER_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };

}