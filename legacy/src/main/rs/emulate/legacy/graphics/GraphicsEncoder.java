package rs.emulate.legacy.graphics;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveEntry;

import java.util.List;

/**
 * A base class for graphics archive entry encoders.
 *
 * @author Major
 */
public abstract class GraphicsEncoder {

	/**
	 * The name of archive entry.
	 */
	protected final String name;

	/**
	 * Creates the graphics encoder.
	 *
	 * @param name The name of the archive entry.
	 */
	public GraphicsEncoder(String name) {
		this.name = name;
	}

	/**
	 * Encodes the {@link List} of {@code T}s into an {@link ArchiveEntry}, and returns a new {@link Archive}
	 * containing
	 * the entries in the specified archive, and the newly created one.
	 *
	 * @param archive The archive to base the new archive from.
	 * @return The new archive.
	 */
	public abstract Archive encodeInto(Archive archive);

}