package rs.emulate.legacy.version;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.collect.ImmutableList;

/**
 * Decodes ArchiveEntry objects from the version {@link Archive} containing file CRC data.
 *
 * @author Major
 */
public final class CrcDecoder {

	/**
	 * The version Archive.
	 */
	private final Archive version;

	/**
	 * The names of the CRC ArchiveEntries.
	 */
	private final List<String> names;

	/**
	 * Creates the CrcDecoder.
	 *
	 * @param version The {@link Archive} containing the version data.
	 * @param names The names of the CRC {@link ArchiveEntry} objects.
	 */
	public CrcDecoder(Archive version, List<String> names) {
		this.version = version;
		this.names = names;
	}

	/**
	 * Decodes the file {@link CrcList}s.
	 * 
	 * @return The {@link List} of file CrcLists.
	 * @throws FileNotFoundException If any of the {@link ArchiveEntry} names could not be found.
	 */
	public List<CrcList> decode() throws FileNotFoundException {
		int size = names.size();
		List<CrcList> lists = new ArrayList<>(size);

		for (int type = 0; type < size; type++) {
			String name = names.get(type);
			ArchiveEntry entry = version.getEntry(name);
			DataBuffer data = entry.getBuffer();

			int count = data.limit() / Integer.BYTES;
			int[] crcs = new int[count];
			Arrays.setAll(crcs, index -> data.getInt());

			lists.add(new CrcList(crcs));
		}

		return ImmutableList.copyOf(lists);
	}

}