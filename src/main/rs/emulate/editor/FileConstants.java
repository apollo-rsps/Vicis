package rs.emulate.editor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Contains file-related constants.
 * 
 * @author Major
 */
public final class FileConstants {

	/**
	 * The Path to the backup directory.
	 */
	public static final Path BACKUP_DIRECTORY;

	/**
	 * The Path to the configuration directory.
	 */
	public static final Path CORE_DIRECTORY;

	static {
		Path path = Paths.get(System.getProperty("user.home"), "/");
		boolean windows = System.getProperty("os.name").contains("Windows");

		CORE_DIRECTORY = windows ? path.resolve("AppData/Roaming/EmulateRS/Vicis") : path.resolve(".vicis");
		BACKUP_DIRECTORY = CORE_DIRECTORY.resolve("backup");

		if (!Files.exists(CORE_DIRECTORY)) {
			try {
				Files.createDirectory(CORE_DIRECTORY);
				Files.createDirectory(BACKUP_DIRECTORY);
			} catch (IOException e) {
				throw new UncheckedIOException("Failed to create directories.", e);
			}
		}

	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private FileConstants() {

	}

}