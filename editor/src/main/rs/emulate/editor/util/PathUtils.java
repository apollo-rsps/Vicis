package rs.emulate.editor.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Contains {@link Path}-related utility methods.
 *
 * @author Major
 */
public final class PathUtils {

	/**
	 * Indicates that the current operating system is a version of windows.
	 */
	public static final boolean ON_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

	/**
	 * The storage {@link Path} for other users.
	 */
	private static final Path OTHER_PATH = Paths.get(System.getProperty("user.home"), ".vicis");

	/**
	 * The storage {@link Path} for Windows users.
	 */
	private static final Path WINDOWS_PATH = Paths.get(System.getProperty("user.home"), "AppData/Roaming/Vicis");

	static {
		Path storage = getStoragePath();
		try {
			Files.createDirectories(storage);
		} catch (IOException e) {
			throw new UncheckedIOException(
					"Failed to create storage directories. Please ensure vicis has read and write access to the " +
							"following path: " + storage.toAbsolutePath(), e);
		}
	}

	/**
	 * Gets the data storage {@link Path} in use.
	 *
	 * @return The Path.
	 */
	public static Path getStoragePath() {
		return ON_WINDOWS ? WINDOWS_PATH : OTHER_PATH;
	}

	/**
	 * Attempts to move the file represented by the specified {@link Path} to the specified destination atomically,
	 * resorting to moving it non-atomically if atomic operations are not supported by the source or destination file
	 * system.
	 *
	 * @param source The source path.
	 * @param destination The destination path.
	 * @throws IOException If the file could not be moved.
	 */
	public static void move(Path source, Path destination) throws IOException {
		try {
			Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (AtomicMoveNotSupportedException e) {
			Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}

}