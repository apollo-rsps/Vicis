package rs.emulate.legacy.version;

import com.google.common.collect.ImmutableList;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveEntry;
import rs.emulate.shared.util.DataBuffer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Decodes ArchiveEntry objects from the version {@link Archive} containing file version data.
 *
 * @author Major
 */
public final class VersionListDecoder {

	/**
	 * The version Archive.
	 */
	private final Archive versions;

	/**
	 * Creates the VersionListDecoder.
	 *
	 * @param versions The {@link Archive} containing the version data.
	 */
	public VersionListDecoder(Archive versions) {
		this.versions = versions;
	}

	/**
	 * Decodes the file {@link VersionList}s.
	 *
	 * @return The {@link List} of file VersionLists.
	 * @throws FileNotFoundException If any of the {@link ArchiveEntry} names could not be found.
	 */
	public List<VersionList> decode() throws FileNotFoundException {
		List<VersionList> lists = new ArrayList<>(VersionList.VERSION_ENTRY_NAMES.length);

		for (int type = 0; type < VersionList.VERSION_ENTRY_NAMES.length; type++) {
			String name = VersionList.VERSION_ENTRY_NAMES[type];
			ArchiveEntry entry = versions.getEntry(name);
			DataBuffer data = entry.getBuffer();

			int count = data.limit() / Short.BYTES;
			int[] versions = new int[count];
			Arrays.setAll(versions, index -> data.getUnsignedShort());

			lists.add(new VersionList(versions));
		}

		return ImmutableList.copyOf(lists);
	}

}