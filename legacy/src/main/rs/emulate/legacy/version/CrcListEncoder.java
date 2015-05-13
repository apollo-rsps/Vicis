package rs.emulate.legacy.version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

/**
 * Encodes {@link CrcList}s into {@link ArchiveEntry} objects.
 *
 * @author Major
 */
public final class CrcListEncoder {

	/**
	 * The List of CrcLists.
	 */
	private final List<CrcList> lists;

	/**
	 * Creates the CrcListEncoder.
	 *
	 * @param crcs The {@link List} of {@link CrcList}s.
	 */
	public CrcListEncoder(List<CrcList> crcs) {
		this.lists = new ArrayList<>(crcs);
	}

	/**
	 * Encodes the {@link CrcList}s.
	 *
	 * @return The array of {@link ArchiveEntry} objects.
	 */
	public ArchiveEntry[] encode() {
		int size = lists.size();
		ArchiveEntry[] entries = new ArchiveEntry[size];

		for (int index = 0; index < size; index++) {
			CrcList list = lists.get(index);
			VersionEntryType type = list.getType();
			int[] crcs = list.getCrcs();

			DataBuffer buffer = DataBuffer.allocate(crcs.length * Integer.BYTES);
			Arrays.stream(crcs).forEach(buffer::putInt);

			entries[index] = new ArchiveEntry(type.asCrcList(), buffer);
		}

		return entries;
	}

}