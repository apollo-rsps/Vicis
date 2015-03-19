package rs.emulate.util;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import rs.emulate.editor.Settings;

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
	 * Attempts to move the file represented by the specified {@link Path} to the specified destination atomically,
	 * resorting to moving it non-atomically if atomic operations are not supported by the source or destination file
	 * system.
	 * 
	 * @param source The source path.
	 * @param destination The destination path.
	 * @throws IOException If the file could not be moved.
	 */
	public static void attemptAtomicMove(Path source, Path destination) throws IOException {
		try {
			Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (AtomicMoveNotSupportedException e) {
			Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Gets the {@link Path} to the {@code resources} directory.
	 * 
	 * @param settings The application {@link Settings}.
	 * @return The Path.
	 */
	public static Path getResourcesPath(Settings settings) {
		return Paths.get(settings.get("resources-path"));
	}

	/**
	 * Gets the {@link Path} to the data directory.
	 * 
	 * @param settings The application {@link Settings}.
	 * @return The Path.
	 */
	public static Path getDataPath(Settings settings) {
		return Paths.get(settings.get("data-path"));
	}

	/**
	 * Gets the {@link Path} to the backup directory.
	 * 
	 * @param settings The application {@link Settings}.
	 * @return The Path.
	 */
	public static Path getBackupPath(Settings settings) {
		return Paths.get(settings.get("backup-path"));
	}

	/**
	 * Gets the {@link Path} to the private configuration directory.
	 * 
	 * @param settings The application {@link Settings}.
	 * @return The Path.
	 */
	public static Path getConfigPath(Settings settings) {
		return Paths.get(settings.get("config-path"));
	}

}