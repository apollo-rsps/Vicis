package rs.emulate.legacy.graphics;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

import java.io.FileNotFoundException;

/**
 * A base class for graphics ArchiveEntry decoders.
 *
 * @author Major
 */
public abstract class GraphicsDecoder {

	/**
	 * Gets an {@link ArchiveEntry} containing data from the specified {@link Archive}.
	 *
	 * @param graphics The graphics Archive.
	 * @param name The name of the entry, without the {@link GraphicsConstants#DATA_EXTENSION}.
	 * @return The index ArchiveEntry.
	 * @throws FileNotFoundException If the index could not be found.
	 */
	protected static ArchiveEntry getDataEntry(Archive graphics, String name) throws FileNotFoundException {
		return graphics.getEntry(name + GraphicsConstants.DATA_EXTENSION);
	}

	/**
	 * Gets the {@code index.dat} {@link ArchiveEntry} from the specified {@link Archive}.
	 *
	 * @param graphics The graphics Archive.
	 * @return The index ArchiveEntry.
	 * @throws FileNotFoundException If the index could not be found.
	 */
	protected static ArchiveEntry getIndexEntry(Archive graphics) throws FileNotFoundException {
		return graphics.getEntry(GraphicsConstants.INDEX_FILE_NAME);
	}

	/**
	 * The Buffer containing the data.
	 */
	protected final DataBuffer data;

	/**
	 * The Buffer containing the indices.
	 */
	protected final DataBuffer index;

	/**
	 * Creates the GraphicsDecoder.
	 *
	 * @param data The data {@link ArchiveEntry}.
	 * @param index The index ArchiveEntry.
	 * @throws FileNotFoundException If the entries with the specified name could not be found.
	 */
	public GraphicsDecoder(ArchiveEntry data, ArchiveEntry index) throws FileNotFoundException {
		this.data = data.getBuffer().asReadOnlyBuffer();
		this.index = index.getBuffer().asReadOnlyBuffer();
	}

}