package rs.emulate.legacy.archive;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains archive-related utility methods.
 * 
 * @author Major
 */
public final class ArchiveUtils {

	/**
	 * Hashes the specified string into an integer used to identify an {@link ArchiveEntry}.
	 * 
	 * @param name The name of the entry.
	 * @return The hash.
	 */
	public static int hash(String name) {
		return name.toUpperCase().chars().reduce(0, (hash, character) -> hash * 61 + character - 32);
	}

	/**
	 * Gets the size of the data contained in the specified {@link ArchiveEntry} objects.
	 * 
	 * @param entries The archive entries.
	 * @return The size.
	 */
	public static int sizeOf(List<ArchiveEntry> entries) {
		return entries.stream().collect(Collectors.summingInt(ArchiveEntry::getSize));
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private ArchiveUtils() {

	}

}