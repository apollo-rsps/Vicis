package rs.emulate.legacy.version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.Preconditions;

/**
 * Encodes CRC data in the version Archive.
 *
 * @author Major
 */
public final class CrcEncoder {

	/**
	 * The List of CrcLists.
	 */
	private final List<CrcList> crcs;

	/**
	 * The names of the CRC ArchiveEntries.
	 */
	private final List<String> names;

	/**
	 * Creates the CrcEncoder.
	 *
	 * @param crcs The {@link List} of {@link CrcList}s.
	 * @param names The names of the CRC {@link ArchiveEntry} objects.
	 */
	public CrcEncoder(List<CrcList> crcs, List<String> names) {
		Preconditions.checkArgument(crcs.size() == names.size(), "CrcList count and names count must be equal.");

		this.crcs = new ArrayList<>(crcs);
		this.names = new ArrayList<>(names);
	}

	/**
	 * Encodes the {@link CrcList}s.
	 * 
	 * @return The array of {@link ArchiveEntry} objects.
	 */
	public ArchiveEntry[] encode() {
		int size = crcs.size();
		ArchiveEntry[] entries = new ArchiveEntry[size];

		for (int index = 0; index < size; index++) {
			CrcList list = crcs.get(index);
			int[] crcs = list.getCrcs();

			DataBuffer buffer = DataBuffer.allocate(crcs.length * Integer.BYTES);
			Arrays.stream(crcs).forEach(buffer::putInt);

			entries[index] = new ArchiveEntry(names.get(index), buffer);
		}

		return entries;
	}

}